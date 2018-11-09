package com.mongodb.transaction.service.impl;

import com.mongodb.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.transaction.config.MongoDBConifg;
import com.mongodb.transaction.dao.LayoutFileRepository;
import com.mongodb.transaction.domain.LayoutFile;
import com.mongodb.transaction.service.LayoutFileService;
import com.mongodb.transaction.utils.LayoutFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LayoutFileServiceImpl implements LayoutFileService {

    @Autowired
    private MongoDBConifg mongoDBConifg;

    @Autowired
    private LayoutFileUtils layoutFileUtils;

    @Autowired
    private LayoutFileRepository repository;

    private MongoClient client;

    @Override
    public boolean bathInsert(List<LayoutFile> layoutFiles) {
        ClientSession session = getClient().startSession();
        try {
            session.startTransaction(TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
            Map<String, List<LayoutFile>> fileList = layoutFiles.stream().collect(Collectors.groupingBy(LayoutFile::getEntityType));
            for (String layoutFile : fileList.keySet()) {
                List<LayoutFile> temp = fileList.get(layoutFile);
                MongoCollection<LayoutFile> collection = mongoDBConifg.getMongoDatabase().getCollection(layoutFileUtils.getCollectionName(layoutFile), LayoutFile.class);
                repository.bathInsert(session, collection, temp, false);
            }
            session.commitTransaction();
        } catch (MongoCommandException e) {
            session.abortTransaction();
            log.debug("ROLLBACK TRANSACTION ");
            return false;
        } catch (Exception e) {
            log.error("Error bathInsert :" + e);
        } finally {
            session.close();
        }
        return true;
    }

    @Override
    public boolean bathUpdate(List<LayoutFile> layoutFiles) {
        ClientSession session = getClient().startSession();
        try {
            session.startTransaction(TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());
            Map<String, List<LayoutFile>> fileList = layoutFiles.stream().collect(Collectors.groupingBy(LayoutFile::getEntityType));
            for (String layoutFile : fileList.keySet()) {
                List<LayoutFile> temp = fileList.get(layoutFile);
                MongoCollection<LayoutFile> collection = mongoDBConifg.getMongoDatabase().getCollection(layoutFileUtils.getCollectionName(layoutFile), LayoutFile.class);
                repository.bathUpdate(session, collection, temp, false, false);
            }
            session.commitTransaction();
        } catch (MongoCommandException e) {
            session.abortTransaction();
            log.debug("ROLLBACK TRANSACTION ");
            return false;
        } catch (Exception e) {
            log.error("Error bathUpdate :" + e);
        } finally {
            session.close();
        }
        return true;
    }

    /**
     * 批量更新或者插入
     *
     * @param layoutFiles
     * @return
     */
    @Override
    public boolean bathInsertOrUpdate(List<LayoutFile> layoutFiles) {
        ClientSession session = getClient().startSession();
        try {
            session.startTransaction(TransactionOptions.builder().writeConcern(WriteConcern.MAJORITY).build());

            Map<String, List<LayoutFile>> fileList = layoutFiles.stream().collect(Collectors.groupingBy(LayoutFile::getEntityType));
            for (String layoutFile : fileList.keySet()) {
                List<LayoutFile> temp = fileList.get(layoutFile);
                MongoCollection<LayoutFile> collection = mongoDBConifg.getMongoDatabase().getCollection(layoutFileUtils.getCollectionName(layoutFile), LayoutFile.class);
                repository.bathUpsert(session, collection, temp);
            }

            session.commitTransaction();
        } catch (MongoCommandException e) {
            session.abortTransaction();
            log.debug("ROLLBACK TRANSACTION " + e.toString());
            return false;
        } catch (Exception e) {
            session.abortTransaction();
            log.error("Error bathInsertOrUpdate :" + e.toString());
        } finally {
            session.close();
        }
        return true;
    }

    public MongoClient getClient() {
        return mongoDBConifg.getClient();
    }

}
