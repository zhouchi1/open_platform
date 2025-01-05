package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商户入驻附件类型
 *
 * @author 张鑫彬
 * @date 2024-12-17 17:46:18
 */
@Getter
@AllArgsConstructor
public enum EnclosureTypeEnum {

    /**
     * 内部照片
     */
    INTERNAL_PHOTO("INTERNAL_PHOTO", "内部照片"),
    /**
     * 外部照片
     */
    EXTERNAL_PHOTO("EXTERNAL_PHOTO", "外部照片"),
    ;

    private final String type;

    private final String desc;

}
