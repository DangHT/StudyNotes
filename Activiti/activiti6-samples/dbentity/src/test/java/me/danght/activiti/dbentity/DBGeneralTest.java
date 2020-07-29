package me.danght.activiti.dbentity;

import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityImpl;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * ACT_GE_*
 * @author DangHT
 * @date 2020/07/29
 */
public class DBGeneralTest {

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule("activiti-mysql.cfg.xml");

    @Test
    public void testByteArray() {
        activitiRule
                .getRepositoryService()
                .createDeployment()
                .name("测试部署")
                .addClasspathResource("my-process.bpmn20.xml")
                .deploy();
    }

    @Test
    public void testByteArrayInsert() {
        ManagementService managementService = activitiRule.getManagementService();
        managementService.executeCommand(commandContext -> {
            ByteArrayEntityImpl entity = new ByteArrayEntityImpl();
            entity.setName("test");
            entity.setBytes("test message".getBytes());
            commandContext.getByteArrayEntityManager().insert(entity);
            return null;
        });
    }

}
