package com.zhouzhou.cloud.common.service.base;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;

import java.io.Serializable;

/**
 * BASE：全称：Basically Available(基本可用)，Soft state（软状态）,和 Eventually consistent（最终一 致性）
 * AMO：全称：Analysis Management Object（分析管理对象），此模型简称不被阿里规范插件扫描
 * @Author: sunqr
 * @Date: 2022/10/12 17:22
 */
@ProtobufClass
public class BaseAMO implements Serializable {
    private static final long serialVersionUID = 9010049478408415176L;

    public BaseAMO() {
    }
}
