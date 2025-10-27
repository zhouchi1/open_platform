package com.zhouzhou.cloud.common.entity;

import com.zhouzhou.cloud.common.enums.messageservice.MessageKindEnum;
import com.zhouzhou.cloud.common.enums.messageservice.MessageReadStatusEnum;
import com.zhouzhou.cloud.common.service.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-16
 * @Description: 消息中心主表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageCenter extends BaseEntity {

    @Schema(name = "信息编码")
    private String messageCode;

    @Schema(name = "用户编码")
    private String userCode;

    @Schema(name = "消息内容")
    private String message;

    @Schema(name = "消息类型")
    private MessageKindEnum type;

    @Schema(name = "状态 1：已读 0：未读")
    private MessageReadStatusEnum messageStatus;

    @Schema(name = "创建人")
    private String createNo;

    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "修改人")
    private String updateNo;

    @Schema(name = "修改时间")
    private LocalDateTime updateTime;
}
