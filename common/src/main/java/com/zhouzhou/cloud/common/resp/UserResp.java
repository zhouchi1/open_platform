package com.zhouzhou.cloud.common.resp;

import com.zhouzhou.cloud.common.service.base.BaseAMO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class UserResp extends BaseAMO {

    private static final long serialVersionUID = 8922748325213933348L;
    /**
     * 用户名称
     */
    @Schema(name = "用户名称")
    private Long accountId;

    /**
     * 用户名称
     */
    @Schema(name = "用户名称")
    private String name;

    /**
     * 账号
     */
    @Schema(name = "账号")
    private String userAccount;
    /**
     * 手机号
     */
    @Schema(name = "手机号")
    private String mobile;

    /**
     * 账号状态 是否启用
     */
    @Schema(name = "账号状态 是否启用")
    private Boolean status;

    /**
     * 是否是管理员
     */
    @Schema(name = "是否是管理员")
    private Boolean admin;


    /**
     * 钉钉用户编码
     */
    @Schema(name = "钉钉用户编码")
    private String dingUserCode;


    /**
     * 是否首次登录
     */
    @Schema(name = "是否首次登录")
    private Boolean pwdState;

    /**
     * 最新登录时间
     */
    @Schema(name = "最新登录时间")
    private LocalDateTime loginTime;

}
