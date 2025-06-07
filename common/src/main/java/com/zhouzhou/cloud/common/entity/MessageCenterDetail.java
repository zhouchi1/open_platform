package com.zhouzhou.cloud.common.entity;

import com.zhouzhou.cloud.common.enums.message.MessageReceiveEnum;
import com.zhouzhou.cloud.common.service.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
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

    private static final Long serialVersionUID = 7234562768956361523L;

    @ApiModelProperty("消息主表Id")
    private String messageMasterId;

    @ApiModelProperty("接收消息平台唯一标识Id")
    private String receiveMessageAppId;

    @ApiModelProperty("接收消息用户唯一标识Id")
    private String receiveMessageUserId;

    @ApiModelProperty("消息接收状态枚举")
    private MessageReceiveEnum messageReceiveEnum;
}

