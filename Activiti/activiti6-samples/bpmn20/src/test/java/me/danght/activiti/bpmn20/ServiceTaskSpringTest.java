package me.danght.activiti.bpmn20;

import com.google.common.collect.Maps;
import me.danght.activiti.example.MyJavaBean;
import me.danght.activiti.example.MyJavaDelegate;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 基于Spring的服务任务ServiceTask测试
 * @author DangHT
 * @date 2020/07/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti-context.xml")
public class ServiceTaskSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskSpringTest.class);

    @Resource
    @Rule
    public ActivitiRule activitiRule;

    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask() {
        //见activiti-context.xml中配置的bean myJavaDelegate
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-servicetask4");
    }

    @Test
    @Deployment(resources = {"my-process-servicetask4.bpmn20.xml"})
    public void testServiceTask2() {
        //如果对象通过参数传入，则优先使用参数对象，不会再使用SpringBean中配置的对象
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaDelegate myJavaDelegate = new MyJavaDelegate();
        LOGGER.info("myJavaDelegate = {}", myJavaDelegate);
        variables.put("myJavaDelegate", myJavaDelegate);
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-servicetask4", variables);
    }

    @Test
    @Deployment(resources = {"my-process-servicetask5.bpmn20.xml"})
    public void testServiceTask3() {
        //如果对象通过参数传入，则优先使用参数对象，不会再使用SpringBean中配置的对象
        Map<String, Object> variables = Maps.newHashMap();
        MyJavaBean myJavaBean = new MyJavaBean("TEST");
        LOGGER.info("myJavaBean = {}", myJavaBean);
        variables.put("myJavaBean", myJavaBean);
        activitiRule
                .getRuntimeService()
                .startProcessInstanceByKey("my-process-servicetask5", variables);

        List<HistoricActivityInstance> historicActivityInstances = activitiRule
                .getHistoryService()
                .createHistoricActivityInstanceQuery()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .listPage(0, 100);
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            LOGGER.info("activityInstance = {}", historicActivityInstance);
        }
    }

}