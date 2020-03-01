package com.yi.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 * @Author: gwt
 * @Date: 2020/2/23 18:01
 * @Description: 
 */
@TableName("SYS_SCORE")
public class SysScore {

    /** 创建人 */
    @TableField(value = "CREATED_BY ")
    private String createdBy;

    /** 创建时间 */
    @TableField(value = "CREATED_TIME")
    private Date createdTime;

    /** 更新人 */
    @TableField(value = "UPDATED_BY ")
    private String updatedBy;

    /** 更新时间 */
    @TableField(value = "UPDATED_TIME")
    private Date updatedTime;

    /** 主键 */
    @TableId(type = IdType.UUID)
    private String id;

    /** 分数 */
    private Double score;

    /** 教师评语 */
    private String comment;
}