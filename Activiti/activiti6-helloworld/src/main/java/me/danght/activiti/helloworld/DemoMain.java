package me.danght.activiti.helloworld;

import com.google.common.collect.Maps;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 启动类
 * @author DangHT
 * @date 2020/07/22
 */
public class DemoMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoMain.class);

    public static void main(String[] args) throws ParseException {
        LOGGER.info("启动DemoMain");

        //1.创建流程引擎
        ProcessEngine processEngine = getProcessEngine();

        //2.部署流程定义文件
        ProcessDefinition processDefinition = getProcessDefinition(processEngine);

        //3.启动流程
        ProcessInstance processInstance = getProcessInstance(processEngine, processDefinition);

        //4.处理流程任务
        processTask(processEngine, processInstance);
        LOGGER.info("结束DemoMain");
    }

    private static void processTask(ProcessEngine processEngine, ProcessInstance processInstance) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        while (processInstance != null && !processInstance.isEnded()) {
            TaskService taskService = processEngine.getTaskService();
            List<Task> list = taskService.createTaskQuery().list();
            LOGGER.info("待处理任务数量：{}", list.size());
            for (Task task : list) {
                LOGGER.info("待处理任务：{}", task.getName());
                Map<String, Object> variables = getMap(processEngine, scanner, task);
                taskService.complete(task.getId(), variables);
                processInstance = processEngine
                        .getRuntimeService()
                        .createProcessInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .singleResult();
            }
        }
        scanner.close();
    }

    private static Map<String, Object> getMap(ProcessEngine processEngine, Scanner scanner, Task task) throws ParseException {
        FormService formService = processEngine.getFormService();
        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        Map<String, Object> variables = Maps.newHashMap();
        for (FormProperty formProperty : formProperties) {
            String line = null;
            if (formProperty.getType() instanceof StringFormType) {
                LOGGER.info("请输入 {} ?", formProperty.getName());
                line = scanner.nextLine();
                variables.put(formProperty.getId(), line);
            } else if (formProperty.getType() instanceof DateFormType) {
                LOGGER.info("请输入 {} ? 格式（yyyy-MM-dd）", formProperty.getName());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                line = scanner.nextLine();
                Date date = simpleDateFormat.parse(line);
                variables.put(formProperty.getId(), date);
            } else {
                LOGGER.info("类型暂不支持 {}", formProperty.getType());
            }
            LOGGER.info("您输入的内容是：{}", line);
        }
        return variables;
    }

    private static ProcessDefinition getProcessDefinition(ProcessEngine processEngine) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("holiday.bpmn20.xml");
        Deployment deploy = builder.deploy();
        String deployId = deploy.getId();
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .deploymentId(deployId)
                .singleResult();
        LOGGER.info("流程定义文件：{}，流程ID：{}", processDefinition.getName(), processDefinition.getId());
        return processDefinition;
    }

    private static ProcessEngine getProcessEngine() {
        ProcessEngineConfiguration conf = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = conf.buildProcessEngine();
        LOGGER.info("流程引擎名称：{}，版本：{}", processEngine.getName(), ProcessEngine.VERSION);
        return processEngine;
    }

    private static ProcessInstance getProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        LOGGER.info("启动流程：{}", processInstance.getProcessDefinitionKey());
        return processInstance;
    }

}
