package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 入驻状态枚举
 *
 * @author 张鑫彬
 * @date 2024-12-18 10:49:34
 */
@Getter
@AllArgsConstructor
public enum RegistrationStatusEnum {

    /**
     * 待提交审核
     */
    TO_BE_SUBMITTED("TO_BE_SUBMITTED", "待提交审核"),
    /**
     * 待审核
     */
    PENDING("PENDING", "待审核"),
    /**
     * 审核通过
     */
    PASS("PASS", "审核通过"),
    /**
     * 审核不通过
     */
    NOT_PASS("NOT_PASS", "审核不通过"),
    ;

    /**
     * 编码
     */
    private final String code;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 用户提交状态判断
     *
     * @param statusEnum 状态枚举
     */
    public static void verifyAddStatus(RegistrationStatusEnum statusEnum) {
        // 如果状态不为待审核或者待提交审核，则抛出异常
        if (!PENDING.equals(statusEnum) && !TO_BE_SUBMITTED.equals(statusEnum)) {
            throw new IllegalArgumentException("状态不正确");
        }
    }

    /**
     * 用户审核状态判断
     *
     * @param statusEnum 状态枚举
     */
    public static void verifyAuditStatus(RegistrationStatusEnum statusEnum) {
        // 如果状态不为审核通过或者审核不通过，则抛出异常
        if (!PASS.equals(statusEnum) && !NOT_PASS.equals(statusEnum)) {
            throw new IllegalArgumentException("状态不正确");
        }
    }

}
