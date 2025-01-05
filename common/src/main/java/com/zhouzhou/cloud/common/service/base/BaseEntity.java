package com.zhouzhou.cloud.common.service.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author sunqingrui
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 320037810339157464L;

    @Id
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    @Column(name = "create_no")
    private String createNo;


    /**
     * 记录插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人ID
     */
    @TableField(fill = FieldFill.UPDATE)
    @Column(name = "update_no")
    private String updateNo;


    /**
     * 记录最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    @Column(name = "mark")
    private Boolean mark;


    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATE_NO = "createNo";
    public static final String FIELD_CREATE_TIME = "createTime";
    public static final String FIELD_UPDATE_NO = "updateNo";
    public static final String FIELD_UPDATE_TIME = "updateTime";
    public static final String FIELD_MARK = "mark";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATE_NO = "create_no";
    public static final String COLUMN_CREATE_TIME = "create_time";
    public static final String COLUMN_UPDATE_NO = "update_no";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String COLUMN_MARK = "mark";


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCreateNo() {
        return createNo;
    }

    public void setCreateNo(String createNo) {
        this.createNo = createNo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getUpdateNo() {
        return updateNo;
    }

    public void setUpdateNo(String updateNo) {
        this.updateNo = updateNo;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getMark() {
        return mark;
    }

    public void setMark(Boolean mark) {
        this.mark = mark;
    }
}
