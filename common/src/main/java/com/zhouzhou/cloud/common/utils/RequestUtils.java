package com.zhouzhou.cloud.common.utils;

import com.google.common.collect.Maps;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author sunqingrui
 * @date ：2019/05/10
 * @description :
 */
public class RequestUtils {
  public static HttpServletRequest getRequest() {
    ServletRequestAttributes attributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attributes.getRequest();
  }

  public static Map<String, String> getHeaders() {
    Map<String, String> map = Maps.newHashMap();

    HttpServletRequest request = getRequest();

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      map.put(headerName, headerValue);
    }
    return map;
  }
}
