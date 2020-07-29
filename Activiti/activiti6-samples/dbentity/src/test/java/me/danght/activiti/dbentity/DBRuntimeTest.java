package me.danght.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

/**
 * ACT_RU_*
 * @author DangHT
 * @date 2020/07/29
 */
public class DBRuntimeTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testRuntime() {
        activitiRule
                .getRepositoryService()
                .createDeployment()
                .name("请假流程")
                .addClasspathResource("holiday.bpmn20.xml")
                .deploy();

        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("holiday", variables);
    }

    @Test
    public void testSetOwner() {
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processDefinitionKey("holiday").singleResult();

        taskService.setOwner(task.getId(), "user1");
    }

    /**
     * 基于流程定义的Message
     * 只要收到Message就可以启动一个流程实例
     */
    @Test
    public void testMessage() {
        activitiRule
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource("my-process-message.bpmn20.xml")
                .deploy();
    }

    /**
     * 基于流程实例的Message
     * 只有启动了流程实例才可以在实例中监听Message
     */
    @Test
    public void testMessageReceived() {
        activitiRule
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource("my-process-message-received.bpmn20.xml")
                .deploy();

        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-message-received");
    }

    @Test
    public void testJob() throws InterruptedException {
        activitiRule
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource("my-process-job.bpmn20.xml")
                .deploy();

        Thread.sleep(1000 * 30L);
    }
}
