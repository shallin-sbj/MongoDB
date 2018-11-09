package com.mongodb.transaction.controller;

import com.mongodb.transaction.domain.LayoutFieMessage;
import com.mongodb.transaction.domain.LayoutFile;
import com.mongodb.transaction.service.LayoutFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class BathController {

    @Autowired
    private LayoutFileService layoutFileService;

    /**
     * 批量插入
     *
     * @param layoutFieMessage
     */
    @RequestMapping(value = "/bathinsert")
    public String bathInsert(@RequestBody LayoutFieMessage layoutFieMessage) {
        log.info("receive info:" + layoutFieMessage);
        boolean result = false;
        List<LayoutFile> layoutFiles = layoutFieMessage.getLayoutFiles();
        if (!CollectionUtils.isEmpty(layoutFiles)) {
            result = layoutFileService.bathInsert(layoutFiles);
        }
        if (result) {
            return "ok";
        } else {
            return "error";
        }
    }

    /**
     * 批量更新
     *
     * @param layoutFieMessage
     */
    @RequestMapping(value = "/bathupdate")
    public String bathUpdate(@RequestBody LayoutFieMessage layoutFieMessage) {
        List<LayoutFile> layoutFiles = layoutFieMessage.getLayoutFiles();
        boolean result = false;
        if (!CollectionUtils.isEmpty(layoutFiles)) {
            result = layoutFileService.bathUpdate(layoutFiles);
        }
        if (result) {
            return "ok";
        } else {
            return "error";
        }
    }

    /**
     * 批量插入更新
     *
     * @param layoutFieMessage
     */
    @RequestMapping(value = "/bathupsert")
    public String bathInsertOrUpdate(@RequestBody LayoutFieMessage layoutFieMessage) {
        List<LayoutFile> layoutFiles = layoutFieMessage.getLayoutFiles();
        boolean result = false;
        if (!CollectionUtils.isEmpty(layoutFiles)) {
            result = layoutFileService.bathInsertOrUpdate(layoutFiles);
        }
        if (result) {
            return "ok";
        } else {
            return "error";
        }
    }
}
