package me.danght.activiti.dbentity;

import com.google.common.collect.Lists;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * @author DangHT
 * @date 2020/07/29
 */
public class DBConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConfigTest.class);

    @Test
    public void testDBConfig() {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();

        Map<String, Long> tableCount = managementService.getTableCount();
        ArrayList<String> tableNames = Lists.newArrayList(tableCount.keySet());
        Collections.sort(tableNames);
        for (String tableName : tableNames) {
            LOGGER.info("table = {}", tableName);
        }
        LOGGER.info("tableNames.size = {}", tableNames.size());
    }

    @Test
    public void dropTable() {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti-mysql.cfg.xml")
                .buildProcessEngine();
        ManagementService managementService = processEngine.getManagementService();

        managementService.executeCommand(commandContext -> {
            commandContext.getDbSqlSession().dbSchemaDrop();
            LOGGER.info("删除表结构");
            return null;
        });
    }

}
