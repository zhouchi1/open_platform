package com.zhouzhou.cloud.websocketservice.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-04-05
 * @Description: 用户信息返回对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AllUserNodeResp extends BaseAMO {

    private static final long serialVersionUID = 6375326536284658326L;

    @Schema(name ="节点信息")
    private String nodeInfo;

    @Schema(name = "节点所有用户信息")
    private List<AllUserInfoResp> nodeAllUserInfoRespList;
}
