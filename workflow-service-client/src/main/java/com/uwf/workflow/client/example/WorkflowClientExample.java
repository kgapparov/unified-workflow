package com.uwf.workflow.client.example;

import com.uwf.workflow.client.WorkflowClient;
import com.uwf.workflow.client.WorkflowClientBuilder;
import com.uwf.workflow.client.model.*;
import com.uwf.workflow.common.model.Workflow;
import reactor.core.publisher.Mono;

/**
 * Example demonstrating how to use the WorkflowClient.
 */
public class WorkflowClientExample {
    
    public static void main(String[] args) {
        // Example 1: Using the simple WorkflowClient with default configuration
        System.out.println("=== Example 1: Simple WorkflowClient ===");
        WorkflowClient client1 = new WorkflowClient();
        runExample(client1);
        
        // Example 2: Using WorkflowClientBuilder for custom configuration
        System.out.println("\n=== Example 2: Custom WorkflowClient ===");
        WorkflowClient client2 = new WorkflowClient(
            WorkflowClientBuilder.create()
                .baseUrl("http://localhost:8080")
                .clientType(WorkflowClientBuilder.ClientType.REST)
                .build()
        );
        runExample(client2);
        
        // Example 3: Direct usage of the service interface
        System.out.println("\n=== Example 3: Direct Service Usage ===");
        WorkflowClient client3 = new WorkflowClient("http://localhost:8080");
        runExample(client3);
    }
    
    private static void runExample(WorkflowClient client) {
        // Create a simple workflow
        Workflow workflow = new Workflow("Example Workflow");
        
        // Submit workflow
        client.submitWorkflow(workflow)
            .doOnNext(output -> {
                if (output.isSuccess()) {
                    System.out.println("Workflow submitted successfully:");
                    System.out.println("  Run ID: " + output.getRunId());
                    System.out.println("  Workflow ID: " + output.getWorkflowId());
                    System.out.println("  Message: " + output.getMessage());
                    
                    // Get workflow status
                    client.getWorkflowStatus(output.getRunId())
                        .doOnNext(statusOutput -> {
                            if (statusOutput.isSuccess()) {
                                System.out.println("Workflow status retrieved:");
                                System.out.println("  Status: " + statusOutput.getWorkflowContext().status());
                                System.out.println("  Current Step: " + statusOutput.getWorkflowContext().currentStepIndex());
                            } else {
                                System.out.println("Failed to get workflow status: " + statusOutput.getErrorMessage());
                            }
                        })
                        .subscribe();
                        
                    // List workflows
                    client.listWorkflows()
                        .doOnNext(listOutput -> {
                            if (listOutput.isSuccess()) {
                                System.out.println("Total workflows: " + listOutput.getWorkflows().size());
                            } else {
                                System.out.println("Failed to list workflows: " + listOutput.getErrorMessage());
                            }
                        })
                        .subscribe();
                } else {
                    System.out.println("Failed to submit workflow: " + output.getErrorMessage());
                }
            })
            .subscribe();
        
        // Submit workflow by ID
        client.submitWorkflowById("test-workflow-id")
            .doOnNext(output -> {
                if (output.isSuccess()) {
                    System.out.println("Workflow by ID submitted successfully:");
                    System.out.println("  Run ID: " + output.getRunId());
                } else {
                    System.out.println("Failed to submit workflow by ID: " + output.getErrorMessage());
                }
            })
            .subscribe();
        
        // Get workflow data
        client.getWorkflowData("test-run-id")
            .doOnNext(output -> {
                if (output.isSuccess()) {
                    System.out.println("Workflow data retrieved, size: " + output.getWorkflowData().size());
                } else {
                    System.out.println("Failed to get workflow data: " + output.getErrorMessage());
                }
            })
            .subscribe();
        
        // Cancel workflow
        client.cancelWorkflow("test-run-id")
            .doOnNext(output -> {
                if (output.isSuccess()) {
                    System.out.println("Cancel workflow result: " + output.isCanceled());
                } else {
                    System.out.println("Failed to cancel workflow: " + output.getErrorMessage());
                }
            })
            .subscribe();
            
        // Get workflow definition
        client.getWorkflowDefinition("test-workflow-id")
            .doOnNext(output -> {
                if (output.isSuccess()) {
                    System.out.println("Workflow definition retrieved: " + 
                                     (output.getWorkflow() != null ? output.getWorkflow().getId() : "null"));
                } else {
                    System.out.println("Failed to get workflow definition: " + output.getErrorMessage());
                }
            })
            .subscribe();
    }
}
