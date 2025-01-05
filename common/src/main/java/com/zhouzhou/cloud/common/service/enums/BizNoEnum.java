package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.MessageFormat;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2024-11-29
 * @Description: Redis Key Factory
 */
@Getter
@AllArgsConstructor
public enum BizNoEnum {

    PRODUCT_PICTURE_BIZ_NO("daily_orange:product_picture_biz_no",null,"商品图片编码"),

    PRODUCT_PICTURE_GROUP_BIZ_NO("daily_orange:product_picture_group_biz_no",null,"商品图片分组编码"),

    PRODUCT_PICTURE_GROUP_DETAIL_BIZ_NO("daily_orange:product_picture_group_detail_biz_no",null,"商品图片分组详情编码"),

    WX_ORDER_BIZ_NO("daily_orange:wx_order_biz_no",null,"订单前缀"),

    USER_CODE_BIZ_NO("daily_orange:user_code_biz_no",null,"用户编码"),

    /**
     * 租户编码生成递增
     */
    TENANT_CODE_BIZ_NO("daily_orange:tenant_code_biz_no", null, "租户编码"),
    ;

    private final String key;
    private final Long expire;
    private final String desc;

    public String createKey(Object... obj) {
        return MessageFormat.format(this.getKey(), obj);
    }
}
