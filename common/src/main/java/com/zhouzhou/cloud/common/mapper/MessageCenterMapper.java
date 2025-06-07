package com.zhouzhou.cloud.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouzhou.cloud.common.entity.MessageCenter;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Sr.Zhou
 * @CreateTime: 2025-05-16
 * @Description: 消息中心数据库访问层
 */
@Mapper
public interface MessageCenterMapper extends BaseMapper<MessageCenter> {


}
