package com.zhouzhou.cloud.common.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.exception.NacosException;
import com.zhouzhou.cloud.common.service.excepetions.BizException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.url.spi.ShardingSphereURLLoader;

import java.util.Properties;


/**
 * 实现SPI，读取远程的nacos配置
 */
@Slf4j
public class CustomNacosURLLoader implements ShardingSphereURLLoader {


    private static final Long TIMEOUT = 3000L;

    @Override
    @SneakyThrows(BizException.class)
    public String load(final String configurationSubject, final Properties queryProps) {
        NacosConfigManager nacosConfigManager = SpringContextHolder.getApplicationContext().getBean(NacosConfigManager.class);
        try {
            String content = nacosConfigManager.getConfigService().getConfig(configurationSubject ,queryProps.getProperty(Constants.GROUP, Constants.DEFAULT_GROUP), TIMEOUT);
            return content.replaceAll("(?<=- )#", " "); // 替换掉额外加的#符号
        } catch (NacosException e) {
            log.error("加载异常:", e);
        }
        return "";
    }

    @Override
    public String getType() {
        return "nacos:";
    }
}
