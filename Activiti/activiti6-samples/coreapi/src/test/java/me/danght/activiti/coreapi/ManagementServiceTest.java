package me.danght.activiti.coreapi;

import me.danght.activiti.mapper.MyCustomMapper;
import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.cmd.AbstractCustomSqlExecution;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.runtime.DeadLetterJobQuery;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.SuspendedJobQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author DangHT
 * @date 2020/07/28
 */
public class ManagementServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti_job.cfg.xml");

    @Test
    @Deployment(resources = {"my-process-job.bpmn20.xml"})
    public void testManagementService() {
        ManagementService managementService = activitiRule.getManagementService();
        List<Job> jobList = managementService
                .createTimerJobQuery()
                .listPage(0, 100);
        for (Job job : jobList) {
            LOGGER.info("job = {}", ToStringBuilder.reflectionToString(job, ToStringStyle.JSON_STYLE));
        }

        JobQuery jobQuery = managementService.createJobQuery();
        SuspendedJobQuery suspendedJobQuery = managementService.createSuspendedJobQuery();
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery();
    }

    @Test
    @Deployment(resources = {"my-process-job.bpmn20.xml"})
    public void testTablePageQuery() {
        ManagementService managementService = activitiRule.getManagementService();
        TablePage tablePage = managementService
                .createTablePageQuery()
                .tableName(managementService.getTableName(ProcessDefinitionEntity.class))
                .listPage(0, 100);

        List<Map<String, Object>> rows = tablePage.getRows();
        for (Map<String, Object> row : rows) {
            LOGGER.info("row = {}", row);
        }
    }

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCustomSql() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        ManagementService managementService = activitiRule.getManagementService();
        List<Map<String, Object>> mapList = managementService.executeCustomSql(
                new AbstractCustomSqlExecution<MyCustomMapper, List<Map<String, Object>>>(MyCustomMapper.class) {
                    @Override
                    public List<Map<String, Object>> execute(MyCustomMapper mapper) {
                        return mapper.findAll();
                    }
                });
        for (Map<String, Object> map : mapList) {
            LOGGER.info("map = {}", map);
        }
    }

    @Test
    @Deployment(resources = {"my-process.bpmn20.xml"})
    public void testCommand() {
        activitiRule.getRuntimeService().startProcessInstanceByKey("my-process");
        ManagementService managementService = activitiRule.getManagementService();
        ProcessDefinitionEntity processDefinitionEntity = managementService
                .executeCommand(commandContext -> commandContext
                        .getProcessDefinitionEntityManager()
                        .findLatestProcessDefinitionByKey("my-process"));
        LOGGER.info(
                "processDefinitionEntity = {}",
                ToStringBuilder.reflectionToString(processDefinitionEntity, ToStringStyle.JSON_STYLE)
        );
    }
}
