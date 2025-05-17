package com.zhouzhou.cloud.common.entity;

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
 * @Description: 消息中心表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageCenter extends BaseEntity {

    private static final Long serialVersionUID = 7234562768956361523L;

    @ApiModelProperty("saas平台内部唯一识别类型")
    private String saasPlatformType;

    @ApiModelProperty("saas平台外部唯一识别类型")
    private String appId;

    @ApiModelProperty("聊天类型")
    private Boolean chatType;

    @ApiModelProperty("发送者ID")
    private String senderId;

    @ApiModelProperty("接收者ID / 群ID")
    private String receiverId;

    @ApiModelProperty("消息类型")
    private Boolean msgType;

    @ApiModelProperty("消息内容")
    private String msgBody;

    @ApiModelProperty("消息发送时间")
    private LocalDateTime msgTime;

    @ApiModelProperty("消息状态")
    private Integer status;
}
