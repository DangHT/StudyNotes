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

import java.util.List;
import java.util.Map;

/**
 * @author DangHT
 * @date 2020/07/30
 */
public class GatewayTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-exclusiveGateway.bpmn20.xml"})
    public void testExclusiveGateway() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("score", 70);
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-exclusiveGateway", variables);

        Task task = activitiRule
                .getTaskService()
                .createTaskQuery()
                .singleResult();
        LOGGER.info("task.name = {}", task.getName());
    }

    @Test
    @Deployment(resources = {"my-process-parallelGateway.bpmn20.xml"})
    public void testParallelGateway() {
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-parallelGateway");

        List<Task> taskList = activitiRule
                .getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .listPage(0, 100);
        for (Task task : taskList) {
            LOGGER.info("task.name = {}", task.getName());
            activitiRule.getTaskService().complete(task.getId());
        }
        LOGGER.info("taskList.size = {}", taskList.size());

        Task task = activitiRule
                .getTaskService()
                .createTaskQuery()
                .singleResult();
        LOGGER.info("task = {}", task);
    }

}