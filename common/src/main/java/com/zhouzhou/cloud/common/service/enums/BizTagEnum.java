package com.zhouzhou.cloud.common.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 审批业务类型
 *
 * @author sunqingrui
 */
@AllArgsConstructor
@Getter
public enum BizTagEnum {
    /**
     * 要货单
     */
    PRODUCER_WMS_INDENT_SHEET_TAG("PRODUCER_WMS_INDENT_SHEET_TAG", "要货单MQ-TAG"),
    /**
     * 商品信息同步
     */
    PRODUCER_WMS_PRODUCT_TAG("PRODUCER_WMS_PRODUCT_TAG", "商品信息MQ-TAG"),
    /**
     * 仓库线路信息同步
     */
    PRODUCER_WMS_STOCK_ROUTE_TAG("PRODUCER_WMS_STOCK_ROUTE_TAG", "仓库线路信息MQ-TAG"),
    /**
     * 门店线路信息同步
     */
    PRODUCER_WMS_STORE_ROUTE_TAG("PRODUCER_WMS_STORE_ROUTE_TAG", "门店线路信息MQ-TAG"),
    /**
     * 物流箱信息同步
     */
    PRODUCER_WMS_LOGISTICS_BOX_TAG("PRODUCER_WMS_LOGISTICS_BOX_TAG", "物流箱信息MQ-TAG"),
    /**
     * 供应商信息同步
     */
    PRODUCER_WMS_SUPPLIER_TAG("PRODUCER_WMS_SUPPLIER_TAG", "供应商信息MQ-TAG"),
    /**
     * 商品分类信息同步
     */
    PRODUCER_WMS_PRODUCT_CATEGORY_TAG("PRODUCER_WMS_PRODUCT_CATEGORY_TAG", "商品分类信息MQ-TAG"),
    /**
     * 商品品牌信息同步
     */
    PRODUCER_WMS_PRODUCT_BRAND_TAG("PRODUCER_WMS_PRODUCT_BRAND_TAG", "商品品牌信息MQ-TAG"),
    /**
     * 司机信息同步
     */
    PRODUCER_WMS_DRIVER_TAG("PRODUCER_WMS_DRIVER_TAG", "司机信息MQ-TAG"),
    /**
     * 一品多码信息同步
     */
    PRODUCER_WMS_PRODUCT_BAR_MORE_TAG("PRODUCER_WMS_PRODUCT_BAR_MORE_TAG", "一品多码信息MQ-TAG"),
    /**
     * 一品多商信息同步
     */
    PRODUCER_WMS_PRODUCT_SUPPLIER_TAG("PRODUCER_WMS_PRODUCT_SUPPLIER_TAG", "一品多商信息MQ-TAG"),
    /**
     * 千云拣货完成，wms同步拣货
     */
    PRODUCER_WMS_PICKING_TAG("PRODUCER_WMS_PICKING_TAG", "千云拣货完成，wms同步拣货MQ-TAG"),
    /**
     * 千云装车完成，wms同步装车（临时）
     */
    PRODUCER_WMS_LOADING_SHEET_TAG("PRODUCER_WMS_LOADING_SHEET_TAG", "千云装车完成，wms同步装车MQ-TAG"),
    /**
     * 千云出库完成，wms同步出库（临时）
     */
    PRODUCER_WMS_OUTBOUND_SHEET_TAG("PRODUCER_WMS_OUTBOUND_SHEET_TAG", "千云出库完成，wms同步出库MQ-TAG"),
    /**
     * 门店退货单信息同步
     */
    PRODUCER_STORE_PRODUCT_RETURN_INFO_TAG("PRODUCER_STORE_PRODUCT_RETURN_INFO_TAG", "门店退货单信息同步MQ-TAG"),
    /**
     * 采购退货单审核结果同步
     */
    PRODUCER_PURCHASE_RETURN_INFO_TAG("PRODUCER_PURCHASE_RETURN_INFO_TAG", "采购退货单审核结果同步-TAG"),
    /**
     * 库存信息同步
     */
    STOCK_WMS_LOCATION_TAG("STOCK_WMS_LOCATION_TAG", "库存信息同步-TAG"),
    /**
     * 千云门店收货单
     */
    PRODUCER_IN_ALLOT_INFO_TAG("PRODUCER_IN_ALLOT_INFO_TAG", "千云门店收货单-TAG"),
    PRODUCER_WMS_INBOUND_CHANGE_TAG("PRODUCER_WMS_INBOUND_CHANGE_TAG", "千云门店收货单-TAG"),
    PRODUCER_WMS_STOCK_DIFFERENCE_TAG("PRODUCER_WMS_STOCK_DIFFERENCE_TAG", "千云差异仓、退货仓商品库存-TAG"),

    ;
    private final String bizTag;
    private final String BizName;

    public static BizTagEnum matchCode(String tag) {
        if (Objects.isNull(tag)) {
            return null;
        }
        for (BizTagEnum value : values()) {
            if (Objects.equals(value.bizTag, tag)) {
                return value;
            }
        }
        return null;
    }

}
