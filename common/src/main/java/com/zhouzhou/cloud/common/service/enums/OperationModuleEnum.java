package com.zhouzhou.cloud.common.service.enums;

/**
 * @Description 操作模块枚举
 * @Author sqr
 */
public enum OperationModuleEnum {
	/**
	 * 0-商品
	 */
	PRODUCT(0, "商品"),
	/**
	 * 1-供应商
	 */
	SUPPLIER(1, "供应商"),
	/**
	 * 2-仓库
	 */
	DEPOT(2, "仓库"),
	/**
	 * 3-操作员
	 */
	OPERATOR(3, "操作员"),
	/**
	 * 4-字典
	 */
	DICT(4, "字典"),
	/**
	 * 5-波次
	 */
	WAVE(5, "波次"),
	/**
	 * 6-入库
	 */
	RECEIPT(6, "入库"),
	/**
	 * 7-出库
	 */
	SHIPMENT(7, "出库"),
	/**
	 * 8-任务
	 */
	TASK(8, "任务"),
	/**
	 * 9-承运商
	 */
	CARRIER(9, "承运商"),
	/**
	 * 10-打印机
	 */
	PRINTER(10, "打印机"),
	/**
	 * 11-系统
	 */
	SYSTEM(11, "系统"),
	/**
	 * 12-容器
	 */
	CONTAINER(12, "容器"),

	;

	/**
	 * 枚举值
	 */
	final int value;
	/**
	 * 枚举名称
	 */
	final String display;

	OperationModuleEnum(int value, String display) {
		this.value = value;
		this.display = display;
	}

	public static OperationModuleEnum valueOf(int value) {
		for (OperationModuleEnum c : OperationModuleEnum.values()) {
			if (c.getValue() == value) {
				return c;
			}
		}
		return null;
	}

	public int getValue() {
		return value;
	}

	public String getDisplay() {
		return display;
	}
}
