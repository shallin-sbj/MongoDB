package com.mongodb.transaction.utils;

import org.springframework.stereotype.Component;

@Component
public class LayoutFileUtils {

    /**
     * 库里头分别不同的集合，每个集合批量操作
     *
     * @param layoutFile
     * @return
     */
    public String getCollectionName(String layoutFile) {
        return "layoutFile_" + getPostfixString(layoutFile);
    }

    private String getPostfixString(String layoutFile) {
        if (layoutFile.equals("1")) {
            return "up";
        } else if (layoutFile.equals("2")) {
            return "down";
        } else if (layoutFile.equals("3")) {
            return "inner";
        } else if (layoutFile.equals("4")) {
            return "gray";
        } else if (layoutFile.equals("5")) {
            return "release";
        }
        return "";
    }


}
