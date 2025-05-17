package com.zhouzhou.cloud.common.utils;


import com.zhouzhou.cloud.common.resp.BaseListResp;
import com.zhouzhou.cloud.common.service.base.BaseAMO;
import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.service.excepetions.BizExCode;

import java.util.List;

public class ResponseDataUtil {
    public static <T extends BaseAMO> ResponseData<T> success() {
        ResponseData<T> result = new ResponseData<>();
        result.setData(null);
        return result;
    }

    public static <T extends BaseAMO> ResponseData<T> success(T t) {
        ResponseData<T> result = new ResponseData<>();
        result.setData(t);
        return result;
    }

    public static <T extends BaseAMO> ResponseData<T> success(T t, Integer code) {
        ResponseData<T> result = new ResponseData<>();
        result.setData(t);
        result.setCode(code);
        return result;
    }

    public static <T extends BaseAMO> ResponseData<T> success(T t, Integer code, String message) {
        ResponseData<T> result = new ResponseData<>();
        result.setData(t);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T extends BaseAMO> ResponseData<BaseListResp<T>> list(List<T> records) {
        ResponseData<BaseListResp<T>> result = new ResponseData<>();
        BaseListResp<T> listResp = new BaseListResp<>();
        listResp.setRecords(records);
        result.setData(listResp);
        return result;
    }

    public static <T extends BaseAMO> T getData(ResponseData<T> responseData) {
        return BizExCode.SUCCESS.getCode().equals(responseData.getCode()) ? responseData.getData() : null;
    }
}
