package com.zhouzhou.cloud.common.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhouzhou.cloud.common.entity.MessageCenter;
import com.zhouzhou.cloud.common.service.business.MessageCenterService;
import com.zhouzhou.cloud.common.mapper.MessageCenterMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-16
 * @Description: 消息中心接口实现层
 */
@Service
public class MessageCenterServiceImpl extends ServiceImpl<MessageCenterMapper, MessageCenter>
implements MessageCenterService{

}
