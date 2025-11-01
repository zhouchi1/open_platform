package com.zhouzhou.cloud.common.service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import com.zhouzhou.cloud.common.service.resp.SystemUserResp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @author sunqingrui
 * @date ：2019/11/07
 * @description :
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginDTO extends BaseAMO {


    private static final long serialVersionUID = 7859932538758651243L;

    @Schema(name = "用户信息")
    private SystemUserResp userResp;

}
