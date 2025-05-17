package com.zhouzhou.cloud.websocketservice.controller;

import com.zhouzhou.cloud.common.dto.MessageDTO;
import com.zhouzhou.cloud.common.resp.BaseListResp;
import com.zhouzhou.cloud.common.service.base.ResponseData;
import com.zhouzhou.cloud.common.utils.ResponseDataUtil;
import com.zhouzhou.cloud.websocketservice.config.ChannelConfig;
import com.zhouzhou.cloud.websocketservice.resp.AllUserNodeResp;
import com.zhouzhou.cloud.websocketservice.service.SendMessageService;
import com.zhouzhou.cloud.websocketservice.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-03-26
 * @Description: 测试控制器
 */
@Slf4j
@RestController
public class TestConnectController {

    @Resource
    private UserService userService;

    @Resource
    private SendMessageService sendMessageService;

    @PostMapping("/chat/sendMessage")
    public ResponseData<?> chatSendMessage(@RequestBody MessageDTO messageDTO){
        sendMessageService.sendMessage(messageDTO);
        return ResponseDataUtil.success();
    }

    /**
     * 获取登陆人信息
     */
    @PostMapping("/getAllCurrentUser")
    public ResponseData<BaseListResp<AllUserNodeResp>> getAllCurrentUser() {
        return ResponseDataUtil.success(userService.getAllCurrentUser());
    }

    @PostMapping("/getMachineMemoryInfo")
    public void getMachineMemoryInfo(){
        log.info(ChannelConfig.getChannelMap().toString());
        log.info(ChannelConfig.getChannelUserMap().toString());
    }
}
