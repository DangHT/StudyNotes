package me.danght.activiti.dbentity;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ACT_RE_*
 * @author DangHT
 * @date 2020/07/29
 */
public class DBRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConfigTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testDeploy() {
        activitiRule
                .getRepositoryService()
                .createDeployment()
                .name("请假申请")
                .addClasspathResource("holiday.bpmn20.xml")
                .deploy();
    }

    @Test
    public void testSuspend() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        repositoryService.suspendProcessDefinitionById("holiday:1:5004");
        boolean suspended = repositoryService.isProcessDefinitionSuspended("holiday:1:5004");
        LOGGER.info("suspended = {}", suspended);
    }

}
