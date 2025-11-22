package com.zhouzhou.cloud.websocketservice.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.common.resp.BaseListResp;
import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.utils.ResponseDataUtil;
import com.zhouzhou.cloud.websocketservice.dto.MessageTransportDTO;
import com.zhouzhou.cloud.websocketservice.resp.AllUserNodeResp;
import com.zhouzhou.cloud.websocketservice.service.SendMessageService;
import com.zhouzhou.cloud.websocketservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 开放接口（接口请注意添加验签！！！）
 */
@Slf4j
@RestController
@Tag(name = "消息发送与用户查询")
public class OpenTalkController {

    @Resource
    private SendMessageService sendMessageService;

    @Resource
    private UserService userService;


    /**
     * 发送广播消息
     * @param messageTransportDTO 消息传输对象
     */
    @Operation(description = "发送广播消息")
    @ApiOperationSupport(author = "Sr.Zhou",order = 1)
    @PostMapping("/testSendBroadcastMessage")
    public ResponseData<?> testSendBroadcastMessage(@RequestBody MessageTransportDTO messageTransportDTO){
        sendMessageService.sendBroadcastMessage(messageTransportDTO);
        return ResponseDataUtil.success();
    }

    /**
     * 发送私人消息
     * @param messageDTO 消息传输对象
     */
    @Operation(description = "发送私人消息")
    @ApiOperationSupport(author = "Sr.Zhou",order = 2)
    @PostMapping("/chat/sendMessage")
    public void testSendPrivateMessage(@RequestBody MessageDTO messageDTO){
        sendMessageService.sendPrivateMessage(messageDTO);
    }

    /**
     * 获取登陆人信息
     */
    @Operation(description = "获取登陆人信息")
    @ApiOperationSupport(author = "Sr.Zhou",order = 3)
    @PostMapping("/getAllCurrentUser")
    public ResponseData<BaseListResp<AllUserNodeResp>> getAllCurrentUser() {
        return ResponseDataUtil.success(userService.getAllCurrentUser());
    }
}
