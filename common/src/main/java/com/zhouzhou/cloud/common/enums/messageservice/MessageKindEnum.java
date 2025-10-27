package com.zhouzhou.cloud.common.enums.messageservice;

import com.zhouzhou.cloud.common.enums.EnumInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKindEnum implements EnumInterface {

    /**
     * 入驻消息提示
     */
    JOIN_MESSAGE("JOIN_MESSAGE", "入驻消息提示"),
    /**
     * 拣货箱码打印提示
     */
    PICK_PRINT_MESSAGE("PICK_PRINT_MESSAGE", "拣货箱码打印提示"),
    /**
     * 拣货单生成提示
     */
    PICK_ORDER_MESSAGE("PICK_ORDER_MESSAGE", "拣货单生成提示"),
    /**
     * 出库单生成提示
     */
    OUT_BOUND_MESSAGE("OUT_BOUND_MESSAGE", "出库单生成提示"),
    /**
     * 拣货单生成失败提示
     */
    PICK_ORDER_GENERATE_FAILED_MESSAGE("PICK_ORDER_GENERATE_FAILED_MESSAGE", "拣货单生成失败提示"),
    /**
     * 中科自动拉取订单提示
     */
    PULL_ORDER_MESSAGE("PULL_ORDER_MESSAGE", "中科自动拉取订单提示"),
    /**
     * 打印批次标签提示
     */
    PRINT_BATCH_LABEL_MESSAGE("PRINT_BATCH_LABEL_MESSAGE", "打印批次标签提示");
    ;

    private final String code;

    private final String desc;

}
