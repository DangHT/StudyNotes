package me.danght.activiti.bpmn20;

import static org.assertj.core.api.Assertions.assertThat;
import com.google.common.collect.Maps;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 边界错误测试
 * @author DangHT
 * @date 2020/07/30
 */
public class BoundaryErrorEventTest extends PluggableActivitiTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Authentication.setAuthenticatedUserId("DangHT");
    }

    @Override
    protected void tearDown() throws Exception {
        Authentication.setAuthenticatedUserId(null);
        super.tearDown();
    }

    @Deployment(resources = {"reviewSalesLead.bpmn20.xml"})
    public void testReviewSalesLeadProcess() {
        // After starting the process, a task should be assigned to the
        // 'initiator' (normally set by GUI)
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("details", "very interesting");
        variables.put("customerName", "Alfresco");
        String procId = runtimeService.startProcessInstanceByKey("reviewSaledLead", variables).getId();
        Task task = taskService.createTaskQuery().taskAssignee("DangHT").singleResult();
        assertThat(task.getName()).isEqualTo("Provide new sales lead");

        // After completing the task, the review subprocess will be active
        taskService.complete(task.getId());
        Task ratingTask = taskService.createTaskQuery().taskCandidateGroup("accountancy").singleResult();
        assertThat(ratingTask.getName()).isEqualTo("Review customer rating");
        Task profitabilityTask = taskService.createTaskQuery().taskCandidateGroup("management").singleResult();
        assertThat(profitabilityTask.getName()).isEqualTo("Review profitability");

        // Complete the management task by stating that not enough info was
        // provided
        // This should throw the error event, which closes the subprocess
        variables = new HashMap<String, Object>();
        variables.put("notEnoughInformation", true);
        taskService.complete(profitabilityTask.getId(), variables);

        // The 'provide additional details' task should now be active
        Task provideDetailsTask = taskService.createTaskQuery().taskAssignee("DangHT").singleResult();
        assertThat(provideDetailsTask.getName()).isEqualTo("Provide additional details");

        // Providing more details (ie. completing the task), will activate the
        // subprocess again
        taskService.complete(provideDetailsTask.getId());
        List<Task> reviewTasks = taskService.createTaskQuery().orderByTaskName().asc().list();
        assertThat(reviewTasks.get(0).getName()).isEqualTo("Review customer rating");
        assertThat(reviewTasks.get(1).getName()).isEqualTo("Review profitability");

        // Completing both tasks normally ends the process
        taskService.complete(reviewTasks.get(0).getId());
        variables.put("notEnoughInformation", false);
        taskService.complete(reviewTasks.get(1).getId(), variables);
        assertProcessEnded(procId);
    }
}
