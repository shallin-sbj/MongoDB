package com.mongodb.transaction.service;

import com.mongodb.transaction.domain.LayoutFile;

import java.util.List;

public interface LayoutFileService {

    boolean bathInsert(List<LayoutFile> layoutFiles);

    boolean bathUpdate(List<LayoutFile> layoutFiles);

    boolean bathInsertOrUpdate(List<LayoutFile> layoutFiles);

}
