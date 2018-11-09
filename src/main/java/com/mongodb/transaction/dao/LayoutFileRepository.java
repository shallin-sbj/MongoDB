package com.mongodb.transaction.dao;

import com.alibaba.fastjson.JSON;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import com.mongodb.transaction.domain.LayoutFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class LayoutFileRepository {

    /**
     * @param session
     * @param collection
     * @param layoutFiles
     * @param ordered     如果为真，那么当写失败时，返回时不执行剩余的写操作。如果为false，那么当写入失败时，继续执行剩余的写入操作(如果有的话)。默认值为true。
     * @return
     */
    public BulkWriteResult bathInsert(ClientSession session, MongoCollection<LayoutFile> collection, List<LayoutFile> layoutFiles, boolean ordered) {
        BulkWriteResult bulkWriteResult = null;
        List<InsertOneModel<LayoutFile>> insertOneModels = new ArrayList<>();
        for (LayoutFile layoutFile : layoutFiles) {
            if (log.isDebugEnabled()) {
                log.info("bath insert:" + layoutFile.toString());
            }
            Document document = Document.parse(JSON.toJSONString(layoutFile));
            InsertOneModel<LayoutFile> model = new InsertOneModel(document);
            insertOneModels.add(model);
        }
        try {
            bulkWriteResult = collection.bulkWrite(session, insertOneModels, new BulkWriteOptions().ordered(ordered));
        } catch (Exception e) {
            log.info("insetLayoutFileList  error:", e);
            throw e;
        }
        return bulkWriteResult;
    }

    /**
     * 更新时，会重新刷新数据，若是之前没有数据则添加进去，若是之前有某个属性但是现在实体（文档里头）没有这个属性则也会把这个属性删除掉
     *
     * @param session
     * @param collection
     * @param layoutFiles
     * @param isUpsert    是否支持更新替换 默认是false
     * @param ordered     如果为真，那么当写失败时，返回时不执行剩余的写操作。如果为false，那么当写入失败时，继续执行剩余的写入操作(如果有的话)。默认值为true。
     */
    public BulkWriteResult bathUpdate(ClientSession session, MongoCollection<LayoutFile> collection, List<LayoutFile> layoutFiles, boolean isUpsert, boolean ordered) {
        BulkWriteResult bulkWriteResult = null;
        List<ReplaceOneModel<LayoutFile>> replaceOneModels = new ArrayList<>();
        UpdateOptions updateOptions = new UpdateOptions().upsert(isUpsert);
        for (LayoutFile layoutFile : layoutFiles) {
            Bson filter = new Document("fileId", layoutFile.getFileId());   // 根据fileId 去查询是否有数据，假如有则更新，没有则插入
            if (log.isDebugEnabled()) {
                log.info("filedId:" + layoutFile.toString());
            }
            ReplaceOneModel<LayoutFile> replaceOneModel = new ReplaceOneModel(filter, layoutFile, updateOptions);
            replaceOneModels.add(replaceOneModel);
        }
        try {
            bulkWriteResult = collection.bulkWrite(session, replaceOneModels, new BulkWriteOptions().ordered(ordered));
        } catch (Exception e) {
            log.info("insetLayoutFileList  error:", e);
            throw e;
        }
        return bulkWriteResult;
    }

    /**
     * 批量更新替换
     *
     * @param session
     * @param collection
     * @param layoutFiles
     * @return
     */
    public BulkWriteResult bathUpsert(ClientSession session, MongoCollection<LayoutFile> collection, List<LayoutFile> layoutFiles) {
        BulkWriteResult bulkWriteResult = null;
        UpdateOptions updateOptions = new UpdateOptions().upsert(true);
        List<UpdateOneModel<LayoutFile>> updates = new ArrayList<UpdateOneModel<LayoutFile>>();
        for (LayoutFile layoutFile : layoutFiles) {
            Bson filter = new Document("fileId", layoutFile.getFileId());   // 根据fileId 去查询是否有数据，假如有则更新，没有则插入
            if (log.isDebugEnabled()) {
                log.info("filedId:" + layoutFile.toString());
            }
            UpdateOneModel<LayoutFile> updateOneModel = new UpdateOneModel(filter, convertToBson(layoutFile), updateOptions);
            updates.add(updateOneModel);
        }
        try {
            bulkWriteResult = collection.bulkWrite(session, updates, new BulkWriteOptions().ordered(false));
        } catch (Exception e) {
            log.info("insetLayoutFileList  error:", e);
            throw e;
        }
        return bulkWriteResult;
    }

    /**
     * 构造更新内容
     * $set 修饰的是更新的信息
     * $setOnInsert 只在第一次插入数据时会插入，后边修改更新是不会再次更新
     *
     * @param layoutFile
     * @return
     */
    private Document convertToBson(LayoutFile layoutFile) {
        Document newdoc = new Document();
        newdoc.put("fileId", layoutFile.getFileId());
        newdoc.put("filepath", layoutFile.getFilepath());
        newdoc.put("version", layoutFile.getVersion());
        newdoc.put("channel", layoutFile.getChannel());
        newdoc.put("type", layoutFile.getType());
        newdoc.put("status", layoutFile.getStatus());
        newdoc.put("entityType", layoutFile.getEntityType());
        newdoc.put("updatetime", layoutFile.getUpdatetime());
        newdoc.put("dt", layoutFile.getDt());
        return new Document("$set", newdoc).append("$setOnInsert", new Document("createtime", layoutFile.getCreatetime()));
    }

}
