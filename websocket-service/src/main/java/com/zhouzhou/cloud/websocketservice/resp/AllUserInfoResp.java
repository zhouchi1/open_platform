package com.zhouzhou.cloud.websocketservice.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 用户信息返回实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AllUserInfoResp extends BaseAMO {

    private static final Long serialVersionUID = 6728496746395463894L;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("通道Id")
    private String channelId;
}
