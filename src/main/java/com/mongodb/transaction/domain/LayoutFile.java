package com.mongodb.transaction.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class LayoutFile implements Serializable, Comparable<LayoutFile> {

    /**
     * 布局文件/样式文件ID
     */
    private String fileId;

    /**
     * 类型
     */
    private String type;

    /**
     * 版本号
     */
    private String version;
    /**
     * CDN路径信息路径
     */
    private String filepath;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 状态 0: 无效 1： 有效
     * 默认为有效状态
     */
    private int status;

    /**
     * 文件类型，
     */
    private String entityType;

    private Long createtime;

    private Long updatetime;

    private Integer dt;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LayoutFile that = (LayoutFile) o;
        return status == that.status &&
                Objects.equals(fileId, that.fileId) &&
                Objects.equals(type, that.type) &&
                Objects.equals(version, that.version) &&
                Objects.equals(filepath, that.filepath) &&
                Objects.equals(entityType, that.entityType) &&
                Objects.equals(channel, that.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, type, version, filepath, channel, entityType, status);
    }

    @Override
    public int compareTo(LayoutFile layoutFile) {
        if (layoutFile == null) {
            return 1;
        }
        long ans = Long.valueOf(this.version) - Long.valueOf(layoutFile.getVersion());    // 版本号
        if (ans == 0) {
            int channel = this.channel.compareTo(layoutFile.channel);   // 渠道
            if (channel == 0) {
                int fileId = String.valueOf(this.fileId).compareTo(String.valueOf(layoutFile.getFileId()));  // 文件ID
                if (fileId == 0) {
                    return String.valueOf(this.entityType).compareTo(String.valueOf(layoutFile.getEntityType())); // 发布环境
                }
            }
        } else {
            return (int) ans;
        }
        return 0;
    }

}
