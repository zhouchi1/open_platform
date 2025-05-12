//package com.zhouzhou.cloud.addressservice.filter;
//
//import org.apache.dubbo.rpc.*;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DubboConsumerAuthFilter implements Filter {
//
//    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        // 从 Spring Security 容器里拿 JWT Token
//        Object auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth instanceof JwtAuthenticationToken) {
//            String token = ((JwtAuthenticationToken) auth).getToken().getTokenValue();
//            // 放到 RPC 调用的 attachments 里
//            RpcContext.getClientAttachment().setAttachment("Authorization", "Bearer " + token);
//        }
//        return invoker.invoke(invocation);
//    }
//}
//
