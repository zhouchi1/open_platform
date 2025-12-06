package com.zhouzhou.cloud.common.entity;

import com.zhouzhou.cloud.common.enums.messageservice.MessageReceiveEnum;
import com.zhouzhou.cloud.common.service.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-16
 * @Description: 消息中心详情表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageCenterDetail extends BaseEntity {

    @Schema(name = "消息主表Id")
    private String messageMasterId;

    @Schema(name = "接收消息用户唯一标识Id")
    private String receiveMessageUserId;

    @Schema(name = "消息接收状态")
    private Boolean receiveStatus;
}

