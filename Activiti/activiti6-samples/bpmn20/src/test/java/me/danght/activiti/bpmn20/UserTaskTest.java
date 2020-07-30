package me.danght.activiti.bpmn20;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author DangHT
 * @date 2020/07/30
 */
public class UserTaskTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTaskTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-usertask.bpmn20.xml"})
    public void testUserTask() {
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-usertask");

        TaskService taskService = activitiRule.getTaskService();

        Task task = taskService
                .createTaskQuery()
                .taskCandidateUser("user1")
                .singleResult();
        LOGGER.info("find by user1 task = {}", task);


        Task task1 = taskService
                .createTaskQuery()
                .taskCandidateGroup("group1")
                .singleResult();
        LOGGER.info("find by group1 task = {}", task1);

        taskService.claim(task.getId(), "user2");
        LOGGER.info("claim task.id = {} by user2", task.getId());

        //使用setAssignee强制生效
        //taskService.setAssignee(task.getId(), "user2");

        Task task2 = taskService
                .createTaskQuery()
                .taskCandidateOrAssigned("user1")
                .singleResult();
        LOGGER.info("find by user1 task = {}", task2);

        Task task3 = taskService
                .createTaskQuery()
                .taskCandidateOrAssigned("user2")
                .singleResult();
        LOGGER.info("find by user1 task = {}", task3);
    }

    @Test
    @Deployment(resources = {"my-process-usertask2.bpmn20.xml"})
    public void testUserTask2() {
        ProcessInstance processInstance = activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-usertask2");

        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService
                .createTaskQuery()
                .taskCandidateUser("user1")
                .singleResult();
        LOGGER.info("find by user1 task = {}", task);

        taskService.complete(task.getId());
    }

}
