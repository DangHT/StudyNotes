package me.danght.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 子流程测试
 * @author DangHT
 * @date 2020/08/01
 */
public class SubProcessTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubProcessTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcess() {
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-subprocess1");

        Task task = activitiRule
                .getTaskService()
                .createTaskQuery()
                .singleResult();
        LOGGER.info("task.name = {}", task.getName());
    }

    @Test
    @Deployment(resources = {"my-process-subprocess1.bpmn20.xml"})
    public void testSubProcessThrowError() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("errorFlag", true);
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-subprocess1", variables);

        Task task = activitiRule
                .getTaskService()
                .createTaskQuery()
                .singleResult();
        LOGGER.info("task.name = {}", task.getName());
    }

    @Test
    @Deployment(resources = {"my-process-subprocess2.bpmn20.xml"})
    public void testSunProcessEventDriven() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("errorFlag", true);
        variables.put("key1", "value1");
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-subprocess2", variables);

        Task task = activitiRule
                .getTaskService()
                .createTaskQuery()
                .singleResult();
        LOGGER.info("task.name = {}", task.getName());

        Map<String, Object> variables1 = activitiRule
                .getRuntimeService()
                .getVariables(processInstance.getId());
        LOGGER.info("variables = {}", variables1);
    }

    @Test
    @Deployment(resources = {"my-process-subprocess3.bpmn20.xml", "my-process-check-order.bpmn20.xml"})
    public void testSunProcessCallActivity() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("errorFlag", false);
        variables.put("key0", "value0");
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-subprocess3", variables);

        Task task = activitiRule
                .getTaskService()
                .createTaskQuery()
                .singleResult();
        LOGGER.info("task.name = {}", task.getName());

        Map<String, Object> variables1 = activitiRule
                .getRuntimeService()
                .getVariables(processInstance.getId());
        LOGGER.info("variables = {}", variables1);
    }

}

