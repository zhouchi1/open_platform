package com.zhouzhou.cloud.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouzhou.cloud.common.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
