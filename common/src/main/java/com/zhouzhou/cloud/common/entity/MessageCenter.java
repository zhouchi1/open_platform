package com.zhouzhou.cloud.common.entity;

import com.zhouzhou.cloud.common.enums.message.MessageSendEnum;
import com.zhouzhou.cloud.common.service.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
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

    private static final Long serialVersionUID = 7234562768956361523L;

    @ApiModelProperty("消息Id")
    private String messageId;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("消息发送类型")
    private MessageSendEnum messageSendEnum;
}
