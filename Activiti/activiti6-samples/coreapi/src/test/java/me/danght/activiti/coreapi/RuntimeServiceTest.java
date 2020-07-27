package me.danght.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DangHT
 * @date 2020/07/27
 */
public class RuntimeServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    /**
     * 启动流程的方法 1：processKey
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessByKey() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = {}", processInstance);
    }

    /**
     * 启动流程的方法 2：processId
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testStartProcessById() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessDefinition processDefinition = activitiRule
                .getRepositoryService()
                .createProcessDefinitionQuery()
                .singleResult();
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), variables);
        LOGGER.info("processInstance = {}", processInstance);
    }

    /**
     * 启动流程的方法 3：processInstanceBuilder
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceBuilder() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstanceBuilder processInstanceBuilder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = processInstanceBuilder
                .businessKey("businessKey1")
                .processDefinitionKey("my-process")
                .variables(variables)
                .start();
        LOGGER.info("processInstance = {}", processInstance);
    }

    /**
     *  启动流程的方法 4：message
     */
    @Test
    @Deployment(resources = {"my-process-message.bpmn20.xml"})
    public void testMessageStart() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("my-message");
        LOGGER.info("processInstance = {}", processInstance);
    }

    /**
     * 获取，修改流程中的变量
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testVariables() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        LOGGER.info("processInstance = {}", processInstance);

        runtimeService.setVariable(processInstance.getId(), "key3", "value3");
        runtimeService.setVariable(processInstance.getId(), "key2", "value2_");
        Map<String, Object> variables1 = runtimeService.getVariables(processInstance.getId());
        LOGGER.info("variables1 = {}", variables1);
    }

    /**
     * 测试流程实例查询
     */
    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testProcessInstanceQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HashMap<String, Object> variables = Maps.newHashMap();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        LOGGER.info("processInstance = {}", processInstance);

        ProcessInstance processInstance1 = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        LOGGER.info("processInstance1 = {}", processInstance1);
    }

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testExecutionQuery() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HashMap<String, Object> variables = Maps.newHashMap();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        LOGGER.info("processInstance = {}", processInstance);

        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .listPage(0, 100);
        for (Execution execution : executions) {
            LOGGER.info("execution = {}", execution);
        }
        LOGGER.info("execution.size = {}", executions.size());
    }

    @Test
    @Deployment(resources = {"my-process-trigger.bpmn20.xml"})
    public void testTrigger() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process-trigger");

        Execution execution = runtimeService
                .createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        LOGGER.info("execution = {}", execution);

        runtimeService.trigger(execution.getId());
        execution = runtimeService
                .createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        LOGGER.info("execution = {}", execution);
    }

    @Test
    @Deployment(resources = {"my-process-signal-received.bpmn20.xml"})
    public void testSignalEventReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process-signal-received");

        Execution execution = runtimeService
                .createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("execution = {}", execution);

        runtimeService.signalEventReceived("my-signal");
        execution = runtimeService
                .createExecutionQuery()
                .signalEventSubscriptionName("my-signal")
                .singleResult();
        LOGGER.info("execution = {}", execution);
    }

    @Test
    @Deployment(resources = {"my-process-message-received.bpmn20.xml"})
    public void testMessageEventReceived() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process-message-received");

        Execution execution = runtimeService
                .createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("execution = {}", execution);

        runtimeService.messageEventReceived("my-message", execution.getId());
        execution = runtimeService
                .createExecutionQuery()
                .messageEventSubscriptionName("my-message")
                .singleResult();
        LOGGER.info("execution = {}", execution);
    }
}
