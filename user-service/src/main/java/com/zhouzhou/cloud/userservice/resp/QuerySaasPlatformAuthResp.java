package com.zhouzhou.cloud.userservice.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QuerySaasPlatformAuthResp extends BaseAMO {

    private static final long serialVersionUID = 6127834612786487126L;

    @ApiModelProperty("saas平台唯一识别标识")
    private String appId;

    @ApiModelProperty("平台授权开启状态")
    private Boolean status;

    @ApiModelProperty("Saas平台下属所有用户信息的列表")
    private List<QuerySaasPlatformAuthDetailResp> querySaasPlatformAuthDetailRespList;
}
