package me.danght.activiti.config;

import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试
 * @author DangHT
 * @date 2020/07/23
 */
public class ConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigTest.class);

    /**
     * 依赖 Spring 容器，通过解析 activiti.cfg.xml 创建对象
     */
    @Test
    public void testConfig1() {
        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault();
        LOGGER.info("configuration = {}", engineConfiguration);
    }

    /**
     * 不依赖 Spring，直接 new 出对象
     */
    @Test
    public void testConfig2() {
        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration();
        LOGGER.info("configuration = {}", engineConfiguration);
    }

}
