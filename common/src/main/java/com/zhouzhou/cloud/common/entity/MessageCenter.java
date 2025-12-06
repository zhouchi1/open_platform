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

    private String messageId;

    private String message;

    private String messageType;
}
