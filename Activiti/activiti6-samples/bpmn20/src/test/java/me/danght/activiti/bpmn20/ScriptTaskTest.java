package me.danght.activiti.bpmn20;

import com.google.common.collect.Maps;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

/**
 * @author DangHT
 * @date 2020/07/30
 */
public class ScriptTaskTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-scripttask1.bpmn20.xml"})
    public void testScriptTask1() {
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-scripttask1");

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByVariableName()
                .asc()
                .listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("variable = {}", historicVariableInstance);
        }
        LOGGER.info("variables.size = {}", historicVariableInstances.size());
    }

    @Test
    @Deployment(resources = {"my-process-scripttask2.bpmn20.xml"})
    public void testScriptTask2() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 3);
        variables.put("key2", 5);
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-scripttask2", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByVariableName()
                .asc()
                .listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("variable = {}", historicVariableInstance);
        }
        LOGGER.info("variables.size = {}", historicVariableInstances.size());
    }

    @Test
    @Deployment(resources = {"my-process-scripttask3.bpmn20.xml"})
    public void testScriptTask3() {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", 3);
        variables.put("key2", 5);
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-scripttask3", variables);

        HistoryService historyService = activitiRule.getHistoryService();
        List<HistoricVariableInstance> historicVariableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstance.getId())
                .orderByVariableName()
                .asc()
                .listPage(0, 100);
        for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
            LOGGER.info("variable = {}", historicVariableInstance);
        }
        LOGGER.info("variables.size = {}", historicVariableInstances.size());
    }

    @Test
    public void testScripEngine() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("juel");

        Object eval = scriptEngine.eval("${1 + 2}");

        LOGGER.info("value = {}", eval);
    }

}
