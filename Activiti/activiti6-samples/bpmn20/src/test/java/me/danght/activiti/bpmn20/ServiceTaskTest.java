package me.danght.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.ActivitiEngineAgenda;
import org.activiti.engine.ManagementService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 服务任务ServiceTask测试
 * @author DangHT
 * @date 2020/07/31
 */
public class ServiceTaskTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-servicetask.bpmn20.xml"})
    public void testServiceTask() {
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-servicetask");

        List<HistoricActivityInstance> historicActivityInstances = activitiRule
                .getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("activityInstance = {}", historicActivityInstance);
        }
    }

    @Test
    @Deployment(resources = {"my-process-servicetask2.bpmn20.xml"})
    public void testServiceTask2() {
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-servicetask2");

        List<HistoricActivityInstance> historicActivityInstances = activitiRule
                .getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("activityInstance = {}", historicActivityInstance);
        }

        Execution execution = activitiRule
                .getRuntimeService()
                .createExecutionQuery()
                .activityId("someTask")
                .singleResult();
        LOGGER.info("execution = {}", execution);

        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(commandContext -> {
            ActivitiEngineAgenda agenda = commandContext.getAgenda();
            agenda.planTakeOutgoingSequenceFlowsOperation((ExecutionEntity) execution, false);
            return null;
        });

        historicActivityInstances = activitiRule
                .getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("activityInstance = {}", historicActivityInstance);
        }
    }

    @Test
    @Deployment(resources = {"my-process-servicetask3.bpmn20.xml"})
    public void testServiceTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("desc", "the test java delegate");
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-servicetask3", variables);
    }

}