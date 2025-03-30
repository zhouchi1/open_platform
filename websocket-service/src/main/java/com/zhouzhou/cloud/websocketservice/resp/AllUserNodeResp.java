package com.zhouzhou.cloud.websocketservice.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AllUserNodeResp extends BaseAMO {

    private static final long serialVersionUID = 6375326536284658326L;

    @ApiModelProperty("节点信息")
    private String nodeInfo;

    @ApiModelProperty("节点所有用户信息")
    private List<AllUserInfoResp> nodeAllUserInfoRespList;
}
