package me.danght.activiti.config;

import org.activiti.engine.event.EventLogEntry;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Job测试
 * @author DangHT
 * @date 2020/07/23
 */
public class ConfigJobTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigDBTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    @Test
    @Deployment(resources = {"my-process_job.bpmn20.xml"})
    public void test() throws InterruptedException {
        LOGGER.info("开始");
        List<Job> jobs = activitiRule
                .getManagementService()
                .createTimerJobQuery()
                .listPage(0, 100);
        for (Job job : jobs) {
            LOGGER.info("定时任务 {}， 默认重试次数 = {}", job, job.getRetries());
        }
        LOGGER.info("jobList.size {}", jobs.size());
        Thread.sleep(1000 * 100);
        LOGGER.info("结束");
    }


}
