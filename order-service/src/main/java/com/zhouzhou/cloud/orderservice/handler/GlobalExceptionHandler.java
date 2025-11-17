package com.zhouzhou.cloud.orderservice.handler;

import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.service.enums.ResponseEnum;
import com.zhouzhou.cloud.common.service.excepetions.BizExCode;
import com.zhouzhou.cloud.common.service.excepetions.BizException;
import com.zhouzhou.cloud.common.service.excepetions.IErrorCode;
import com.zhouzhou.cloud.common.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * @author sunqingrui
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData<?> exception(Exception ex) {
        HttpServletRequest request = RequestUtils.getRequest();

        ResponseData<?> result = new ResponseData<>();
        result.setCode(BizExCode.UNKNOWN.getCode());
        result.setMessage("系统异常");

        String url = request.getRequestURL().toString();
        String method = request.getMethod();

        log.error(
                "*****系统异常***** url:[{}] method:[{}] parameter:[{}]",
                url,
                method,
                ex.getMessage());
        ex.printStackTrace();
        return result;
    }

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResponseData<?> exception(BizException ex) {
        HttpServletRequest request = RequestUtils.getRequest();

        ResponseData<?> result = new ResponseData<>();
        if (ex.getBizCode() != null) {
            IErrorCode<?> errCode = ex.getBizCode();
            result.setCode((Integer) errCode.getCode());
            result.setMessage(ex.getMessage());
        } else {
            result.setResponseEnum(ResponseEnum.BUSINESS_ERROR);
            result.setMessage(ex.getMessage());
        }


        String url = request.getRequestURL().toString();
        String method = request.getMethod();

        log.warn(
                "*****业务异常***** url:[{}] method:[{}]  msg:[{}]",
                url,
                method,
                ex.getMessage());
        return result;
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseData<?> validatedBindException(BindException ex) {
        Optional<ObjectError> first = ex.getBindingResult().getAllErrors().stream().findFirst();
        String message = first.map(DefaultMessageSourceResolvable::getDefaultMessage).orElse("参数校验异常");

        HttpServletRequest request = RequestUtils.getRequest();
        log.warn("*****校验异常***** Url:[{}] message: [{}]", request.getRequestURL(), message);
        ResponseData<?> result = new ResponseData<>();
        result.setCode(BizExCode.PARAM_ERROR.getCode());
        result.setMessage(message);
        return result;
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseData<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        HttpServletRequest request = RequestUtils.getRequest();
        String url = request.getRequestURL().toString();
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("-不支持");
        sb.append(e.getMethod());
        sb.append("请求方法，");
        sb.append("支持以下");
        String[] methods = e.getSupportedMethods();
        if (methods != null) {
            sb.append(String.join("、", methods));
        }

        log.error(sb.toString(), e);
        ResponseData<?> result = new ResponseData<>();
        result.setCode(BizExCode.PARAM_ERROR.getCode());
        result.setMessage(sb.toString());
        return result;
    }
    @ExceptionHandler({NullPointerException.class})
    public ResponseData<?> handleNullPointerException(NullPointerException ex) {
        log.error("空指针 ", ex);
        ResponseData<?> result = new ResponseData<>();
        result.setCode(BizExCode.PARAM_ERROR.getCode());
        result.setMessage("数据错误");
        return result;
    }

    @ExceptionHandler({OutOfMemoryError.class})
    public ResponseData<?> handleOomException(OutOfMemoryError ex) {
        log.error("内存不足错误 " + ex.getMessage(), ex);
        ResponseData<?> result = new ResponseData<>();
        result.setCode(BizExCode.PARAM_ERROR.getCode());
        result.setMessage("OOM请联系管理员");
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseData<?> validatedBindException(MethodArgumentNotValidException ex) {
        Optional<ObjectError> first = ex.getBindingResult().getAllErrors().stream().findFirst();
        String message = first.map(DefaultMessageSourceResolvable::getDefaultMessage).orElse("参数校验异常");

        HttpServletRequest request = RequestUtils.getRequest();
        log.warn("*****校验异常***** Url:[{}]  message: [{}]", request.getRequestURL(), message);
        ResponseData<?> result = new ResponseData<>();
        result.setCode(BizExCode.PARAM_ERROR.getCode());
        result.setMessage(message);
        return result;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseData<?> httpMessageNotReadableException(MethodArgumentNotValidException ex) {
        Optional<ObjectError> first = ex.getBindingResult().getAllErrors().stream().findFirst();
        String message = first.map(DefaultMessageSourceResolvable::getDefaultMessage).orElse("参数类型校验异常");

        HttpServletRequest request = RequestUtils.getRequest();
        log.warn("*****校验异常***** Url:[{}]  message: [{}]", request.getRequestURL(), message);
        ResponseData<?> result = new ResponseData<>();
        result.setCode(BizExCode.INVALID_PARAMS.getCode());
        result.setMessage(message);
        return result;
    }
}
