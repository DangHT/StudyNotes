package me.danght.activiti.config;

import org.activiti.engine.logging.LogMDC;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * MDC测试
 * @author DangHT
 * @date 2020/07/23
 */
public class ConfigMDCTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDBTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_mdc.cfg.xml");

    /**
     * 测试在 MDCErrorDelegate 中抛出异常并打印出 MDC
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {
//        LogMDC.setMDCEnabled(true);
        ProcessInstance processInstance = activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        assertNotNull(processInstance);

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        assertEquals("Activiti is awesome!", task.getName());
        activitiRule.getTaskService().complete(task.getId());
    }


}
