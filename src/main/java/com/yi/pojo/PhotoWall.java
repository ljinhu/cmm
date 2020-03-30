package com.yi.pojo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * @Author:
 * @Date: 2020/3/25 16:12
 * @Description: 
 */
@TableName("sys_photo")
public class PhotoWall {
    @TableId(type = IdType.UUID)
    private String id;
    @TableField("index_order")
    private String indexOrder;
    @TableField("file_name")
    private String fileName;

    private String detail;

    private String path;
    @TableField("class_id")
    private String classId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getIndexOrder() {
        return indexOrder;
    }

    public void setIndexOrder(String indexOrder) {
        this.indexOrder = indexOrder;
    }
}