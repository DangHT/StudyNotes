package io.zeebe;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.DeploymentEvent;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import io.zeebe.client.api.worker.JobWorker;

import java.util.HashMap;
import java.util.Map;

/**
 * Zeebe Java Client Quickstart
 * @author DangHT
 * @date 2020/12/02
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        final ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress("127.0.0.1:26500")
                .usePlaintext()
                .build();

        System.out.println("Connected.");

        // Deploy a workflow
        final DeploymentEvent deployment = client.newDeployCommand()
                .addResourceFromClasspath("order-process.bpmn")
                .send()
                .join();

        final int version = deployment.getWorkflows().get(0).getVersion();
        System.out.println("Workflow deployed. Version: " + version);

        // Create a workflow instance
        final Map<String, Object> data = new HashMap<>();
        data.put("orderId", "2345");
        data.put("orderValue", 120);

        final WorkflowInstanceEvent wfInstance = client.newCreateInstanceCommand()
                .bpmnProcessId("order-process")
                .latestVersion()
                .variables(data)
                .send()
                .join();

        final long workflowInstanceKey = wfInstance.getWorkflowInstanceKey();
        System.out.println("Workflow instance created. Key: " + workflowInstanceKey);

        // Work on a job
        final JobWorker jobWorker = client.newWorker()
                .jobType("initiate-payment")
                .handler((jobClient, job) -> {
                    System.out.println("Initiate Payment");

                    jobClient.newCompleteCommand(job.getKey())
                            .send()
                            .join();
                })
                .open();

        // Send Message
        client.newPublishMessageCommand()
                .messageName("payment-received")
                .correlationKey((String) data.get("orderId"))
                .send();

        System.out.println("Send Message.");

        client.newWorker()
                .jobType("ship-without-insurance")
                .handler((jobClient, job) -> {
                    System.out.println("Ship without insurance");

                    jobClient.newCompleteCommand(job.getKey())
                            .send()
                            .join();
                })
                .open();

        client.newWorker()
                .jobType("ship-with-insurance")
                .handler((jobClient, job) -> {
                    System.out.println("Ship with insurance");

                    jobClient.newCompleteCommand(job.getKey())
                            .send()
                            .join();
                })
                .open();

        Thread.sleep(3000);

        client.close();
        System.out.println("Closed.");
    }

}
