//package com.zhouzhou.cloud.addressservice.filter;
//
//import jakarta.annotation.Resource;
//import org.apache.dubbo.rpc.*;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.Jwt;
//
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class DubboProviderAuthFilter implements Filter {
//
//    @Resource
//    private JwtDecoder jwtDecoder;
//
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        String authHeader = (String) RpcContext.getServerAttachment().get("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            try {
//                Jwt jwt = jwtDecoder.decode(token);  // ← 这里会在过期时抛异常
//                // 正常业务认证流程……
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(jwt.getSubject(), token, /* authorities */ List.of());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } catch (JwtException ex) {
//                // Token 无效或过期，直接拒绝调用
//                throw new RpcException(RpcException.VALIDATION_EXCEPTION, "Token expired or invalid", ex);
//            }
//        }
//        try {
//            return invoker.invoke(invocation);
//        } finally {
//            SecurityContextHolder.clearContext();
//        }
//    }
//
//}
