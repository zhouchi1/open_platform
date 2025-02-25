package com.zhouzhou.cloud.common.utils;

import com.zhouzhou.cloud.common.service.excepetions.BizExCode;
import com.zhouzhou.cloud.common.service.excepetions.BizException;
import com.zhouzhou.cloud.common.service.excepetions.IErrorCode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

/**
 * @author sunqingrui
 */
public class BizExUtil {
    public static <T> T notNull(T obj, String message) throws BizException {
        if (!ObjectUtils.isEmpty(obj)) {
            throwBizException(BizExCode.BUSINESS_ERROR, message);
        }
        return obj;
    }

    public static <T> T notNull(T obj, IErrorCode errorCode) throws BizException {
        if (!ObjectUtils.isEmpty(obj)) {
            throwBizException(errorCode);
        }
        return obj;
    }

    public static <T> T isNull(T obj, String message) throws BizException {
        if (obj == null) {
            throwBizException(BizExCode.BUSINESS_ERROR, message);
        }
        return obj;
    }

    public static <T> T isNull(T obj, IErrorCode errorCode) throws BizException {
        if (obj == null) {
            throwBizException(errorCode);
        }
        return obj;
    }

    public static <T> T notNull(T obj, IErrorCode errorCode, String message) throws BizException {
        if (obj == null) {
            throwBizException(errorCode, message);
        }
        return obj;
    }

    public static <T> void requirefalse(boolean flag, String message)
            throws BizException {
        if (flag) {
            throwBizException(BizExCode.BUSINESS_ERROR, message);
        }
    }
    public static <T> void requirefalse(boolean flag, BizExCode bizExCode,String message)
            throws BizException {
        if (flag) {
            throwBizException(bizExCode, message);
        }
    }

    public static <T> void requirefalse(boolean flag, IErrorCode errorCode)
            throws BizException {
        if (flag) {
            throwBizException(errorCode);
        }
    }

    public static <T> void requirefalse(boolean flag, IErrorCode errorCode, String message)
            throws BizException {
        if (flag) {
            throwBizException(errorCode, message);
        }
    }

    public static <T> T requireNull(T obj, String message) throws BizException {
        if (obj != null) {
            throwBizException(BizExCode.BUSINESS_ERROR, message);
        }
        return obj;
    }

    public static <T> T requireNull(T obj, IErrorCode errorCode) throws BizException {
        if (obj != null) {
            throwBizException(errorCode);
        }
        return obj;
    }

    public static <T> T requireNull(T obj, IErrorCode errorCode, String message) throws BizException {
        if (obj != null) {
            throwBizException(errorCode, message);
        }
        return obj;
    }

    public static <T> Collection<T> notEmpty(Collection<T> list, String message)
            throws BizException {
        if (CollectionUtils.isEmpty(list)) {
            throwBizException(BizExCode.BUSINESS_ERROR, message);
        }
        return list;
    }

    public static <T> Collection<T> notEmpty(Collection<T> list, IErrorCode errorCode)
            throws BizException {
        if (CollectionUtils.isEmpty(list)) {
            throwBizException(errorCode);
        }
        return list;
    }

    public static <T> Collection<T> notEmpty(Collection<T> list, IErrorCode errorCode, String message)
            throws BizException {
        if (CollectionUtils.isEmpty(list)) {
            throwBizException(errorCode, message);
        }
        return list;
    }


    public static <T> Collection<T> requireEmpty(Collection<T> list, String message)
            throws BizException {
        if (!CollectionUtils.isEmpty(list)) {
            throwBizException(BizExCode.BUSINESS_ERROR, message);
        }
        return list;
    }

    public static <T> Collection<T> requireEmpty(Collection<T> list, IErrorCode errorCode)
            throws BizException {
        if (!CollectionUtils.isEmpty(list)) {
            throwBizException(errorCode);
        }
        return list;
    }

    public static <T> Collection<T> requireEmpty(Collection<T> list, IErrorCode errorCode, String message)
            throws BizException {
        if (!CollectionUtils.isEmpty(list)) {
            throwBizException(errorCode, message);
        }
        return list;
    }

    private static <T> T throwBizException(IErrorCode errorCode, String message) throws BizException {
        throw new BizException(errorCode, message);
    }

    private static <T> T throwBizException(IErrorCode errorCode) throws BizException {
        throw new BizException(errorCode);
    }
}
