package me.danght.activiti.config;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * EventLog测试
 * @author DangHT
 * @date 2020/07/23
 */
public class ConfigEventLogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDBTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_eventlog.cfg.xml");

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void test() {
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process");

        Task task = activitiRule.getTaskService().createTaskQuery().singleResult();
        activitiRule.getTaskService().complete(task.getId());

        List<EventLogEntry> eventLogEntries = activitiRule
                .getManagementService()
                .getEventLogEntriesByProcessInstanceId(processInstance.getId());
        for (EventLogEntry eventLogEntry : eventLogEntries) {
            LOGGER.info("eventLog.Type = {}, eventLog.Data = {}",
                    eventLogEntry.getType(), new String(eventLogEntry.getData()));
        }
        LOGGER.info("eventLogEntries.size = {}", eventLogEntries.size());
    }


}
