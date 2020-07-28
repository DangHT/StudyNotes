package me.danght.activiti.coreapi;

import com.google.common.collect.Maps;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DangHT
 * @date 2020/07/27
 */
public class TaskServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskService() {
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my task message!!!");
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-task", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        taskService.setVariable(task.getId(), "key1", "value1");
        taskService.setVariableLocal(task.getId(), "localKey1", "localValue1");

        Map<String, Object> taskServiceVariables = taskService.getVariables(task.getId());
        Map<String, Object> taskServiceVariablesLocal = taskService.getVariablesLocal(task.getId());
        Map<String, Object> processVariables = activitiRule.getRuntimeService().getVariables(task.getExecutionId());
        LOGGER.info("taskServiceVariables = {}", taskServiceVariables);
        LOGGER.info("taskServiceVariablesLocal = {}", taskServiceVariablesLocal);
        LOGGER.info("processVariables = {}", processVariables);

        HashMap<String, Object> completeVar = Maps.newHashMap();
        completeVar.put("complete", "ok");
        taskService.complete(task.getId(), completeVar);

        Task task1 = taskService.createTaskQuery().taskId(task.getId()).singleResult();
        LOGGER.info("task1 = {}", task1);
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceUser() {
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my task message!!!");
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-task", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        taskService.setOwner(task.getId(), "DangHT");
        //不建议使用强制指定，会替换掉原有的参数
        //taskService.setAssignee(task.getId(), "Jack");

        List<Task> taskList = taskService
                .createTaskQuery()
                .taskCandidateUser("Jack")
                .taskUnassigned()
                .listPage(0, 100);
        for (Task task1 : taskList) {
            try {
                //使用claim设置assignee, 会先检查是否已有assignee
                //如果没有assignee才会分配
                taskService.claim(task1.getId(), "Jack");
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        //查看当前的人员职责状况
        List<IdentityLink> linksForTask = taskService.getIdentityLinksForTask(task.getId());
        for (IdentityLink identityLink : linksForTask) {
            LOGGER.info("identityLink = {}", identityLink);
        }

        //完成 Jack‘s Task
        List<Task> jackTasks = taskService
                .createTaskQuery()
                .taskAssignee("Jack")
                .listPage(0, 100);
        for (Task jackTask : jackTasks) {
            HashMap<String, Object> vars = Maps.newHashMap();
            vars.put("complete", "ok");
            taskService.complete(jackTask.getId(), vars);
        }

        jackTasks = taskService
                .createTaskQuery()
                .taskAssignee("Jack")
                .listPage(0, 100);
        LOGGER.info("Jack's Task 是否为空 [{}]", CollectionUtils.isEmpty(jackTasks));
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceAttachment() {
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my task message!!!");
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-task", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        taskService.createAttachment(
                "url",
                task.getId(),
                task.getProcessInstanceId(),
                "name",
                "description",
                "/url/test.png");

        List<Attachment> taskAttachments = taskService.getTaskAttachments(task.getId());
        for (Attachment taskAttachment : taskAttachments) {
            LOGGER.info(
                    "taskAttachment = {}",
                    ToStringBuilder.reflectionToString(taskAttachment, ToStringStyle.JSON_STYLE)
            );
        }
    }

    @Test
    @Deployment(resources = {"my-process-task.bpmn20.xml"})
    public void testTaskServiceComment() {
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("message", "my task message!!!");
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-task", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        LOGGER.info("task = {}", ToStringBuilder.reflectionToString(task, ToStringStyle.JSON_STYLE));
        LOGGER.info("task.description = {}", task.getDescription());

        //观察与Comment事件类型的区别
        taskService.setOwner(task.getId(), "DangHT");
        taskService.setAssignee(task.getId(), "Jack");

        taskService.addComment(task.getId(), task.getProcessInstanceId(), "comment1");
        taskService.addComment(task.getId(), task.getProcessInstanceId(), "comment2");

        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for (Comment taskComment : taskComments) {
            LOGGER.info("taskComment = {}", ToStringBuilder.reflectionToString(taskComment, ToStringStyle.JSON_STYLE));
        }

        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for (Event taskEvent : taskEvents) {
            LOGGER.info("taskEvent = {}", ToStringBuilder.reflectionToString(taskEvent, ToStringStyle.JSON_STYLE));
        }
    }

}
