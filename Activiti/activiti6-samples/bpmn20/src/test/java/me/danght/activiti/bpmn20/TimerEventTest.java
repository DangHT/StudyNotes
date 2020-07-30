package me.danght.activiti.bpmn20;

import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 超时边界事件测试
 * @author DangHT
 * @date 2020/07/29
 */
public class TimerEventTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerEventTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    @Deployment(resources = {"my-process-timer-boundary.bpmn20.xml"})
    public void testTimerBoundary() throws InterruptedException {
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-timer-boundary");

        List<Task> taskList = activitiRule
                .getTaskService()
                .createTaskQuery()
                .listPage(0, 100);
        for (Task task : taskList) {
            LOGGER.info("task.name = {}", task.getName());
        }
        LOGGER.info("taskList.size = {}", taskList.size());

        Thread.sleep(1000 * 15);

        taskList = activitiRule
                .getTaskService()
                .createTaskQuery()
                .listPage(0, 100);
        for (Task task : taskList) {
            LOGGER.info("task.name = {}", task.getName());
        }
        LOGGER.info("taskList.size = {}", taskList.size());
    }

}
