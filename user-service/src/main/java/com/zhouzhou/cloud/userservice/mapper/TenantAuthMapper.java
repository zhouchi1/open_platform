package com.zhouzhou.cloud.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhouzhou.cloud.common.entity.TenantAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantAuthMapper extends BaseMapper<TenantAuth> {
}
