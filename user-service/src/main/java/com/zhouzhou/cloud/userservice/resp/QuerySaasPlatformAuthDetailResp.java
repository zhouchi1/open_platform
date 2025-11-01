package com.zhouzhou.cloud.userservice.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QuerySaasPlatformAuthDetailResp extends BaseAMO {

    @Schema(name = "用户名称")
    private String userName;

    @Schema(name = "用户状态")
    private Boolean status;
}
