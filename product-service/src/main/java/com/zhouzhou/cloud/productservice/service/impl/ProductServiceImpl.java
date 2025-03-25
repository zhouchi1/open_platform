package com.zhouzhou.cloud.productservice.service.impl;

import com.google.common.collect.Sets;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionResult;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import com.zhouzhou.cloud.productservice.service.ProductService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {


    @Override
    public void addProductInfoByCartesian() {
        Set<String> set1 = new HashSet<>(Arrays.asList("A", "B"));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(1, 2));
        Set<String> set3 = new HashSet<>(Arrays.asList("C", "D"));
        Set<List<Object>> product = Sets.cartesianProduct(set1, set2, set3);
        System.out.println(product);
    }

    @Override
    public void testDeepSeekAi(String message) {
        // 从环境变量中获取API密钥
        String apiKey = "b5c4f3d5-1177-4adb-b3fc-b6e23cb10de5";
        // 创建ArkService实例
        ArkService arkService = ArkService.builder().apiKey(apiKey)
                .timeout(Duration.ofMinutes(30))// 深度推理模型耗费时间会较长，请您设置较大的超时时间，避免超时导致任务失败。推荐30分钟以上
                .build();
        // 初始化消息列表
        List<ChatMessage> chatMessages = new ArrayList<>();
        // 创建用户消息
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(message)// 设置消息内容
                .build();
        // 将用户消息添加到消息列表
        chatMessages.add(userMessage);
        // 创建聊天完成请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("deepseek-r1-250120")// 需要替换为模型的Model ID
                .messages(chatMessages)// 设置消息列表
                .stream(true)
                .build();
        // 发送聊天完成请求并打印响应
        try {
            // 获取响应并打印每个选择的消息内容
            ChatCompletionResult chatCompletionResult = arkService.createChatCompletion(chatCompletionRequest);
            chatCompletionResult.getChoices().forEach(choice -> log.info(choice.getMessage().stringContent()));

            Integer reasoningTokens = chatCompletionResult.getUsage().getCompletionTokensDetails().getReasoningTokens();
            log.info("推理Tokens: " + reasoningTokens);

            Integer cachedTokens = chatCompletionResult.getUsage().getPromptTokensDetails().getCachedTokens();
            log.info("缓存Tokens: " + cachedTokens);

            long totalTokens = chatCompletionResult.getUsage().getTotalTokens();
            log.info("总Tokens: " + totalTokens);

        } catch (Exception e) {
            log.error("请求失败: " + e.getMessage());
        } finally {
            // 关闭服务执行器
            arkService.shutdownExecutor();
        }
    }
}
