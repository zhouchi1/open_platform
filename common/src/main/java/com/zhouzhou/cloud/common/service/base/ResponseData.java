package com.zhouzhou.cloud.common.service.base;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.zhouzhou.cloud.common.service.enums.ResponseEnum;


/**
 * @Author: sunqr
 * @Date: 2022/10/12 10:05
 */
public class ResponseData<T extends BaseAMO> extends BaseResponseData {
    @Protobuf(
            fieldType = FieldType.STRING,
            order = 1,
            required = true
    )
    private Integer code;
    @Protobuf(
            fieldType = FieldType.STRING,
            order = 2,
            required = false
    )
    private String message;
    @Protobuf(
            fieldType = FieldType.OBJECT,
            order = 4,
            required = false
    )
    private T data;

    public ResponseData() {
        this.code = ResponseEnum.SUCCESS.getCode();
        this.message = ResponseEnum.SUCCESS.getDesc();
    }

    public ResponseData(Integer errorCode, String errorMessage) {
        this.code = errorCode;
        this.message = errorMessage;
    }

    public ResponseData(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getDesc();
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setResponseEnum(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getDesc();
    }

    public void setBizCodeAutoResponseEnum(Integer code) {
        this.code = code;
        this.setResponseEnum(ResponseEnum.BUSINESS_ERROR);
    }
}
