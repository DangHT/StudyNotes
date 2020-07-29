package me.danght.activiti.dbentity;

import com.google.common.collect.Maps;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

/**
 * ACT_HI_*
 * @author DangHT
 * @date 2020/07/29
 */
public class DBHistoryTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testHistory() {
        activitiRule
                .getRepositoryService()
                .createDeployment()
                .name("测试部署")
                .addClasspathResource("my-process.bpmn20.xml")
                .deploy();
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        variables.put("key3", "value3");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process", variables);
        runtimeService.setVariable(processInstance.getId(), "key1", "value1_");

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        taskService.setOwner(task.getId(), "user1");

        taskService.createAttachment(
                "url",
                task.getId(),
                processInstance.getId(),
                "attachment",
                "desc",
                "/url/test.png"
        );

        taskService.addComment(task.getId(), task.getProcessInstanceId(), "record note1");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "record note2");

        Map<String, String> properties = Maps.newHashMap();
        properties.put("key2", "value2_1");
        properties.put("key3", "value3_1");
        activitiRule.getFormService().submitTaskFormData(task.getId(), properties);
    }

}
