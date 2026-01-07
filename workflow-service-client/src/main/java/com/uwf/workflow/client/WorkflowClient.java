package com.uwf.workflow.client;

import com.uwf.workflow.client.model.*;
import com.uwf.workflow.common.model.Workflow;
import reactor.core.publisher.Mono;

/**
 * Main client class for interacting with workflow services.
 * Provides a simplified API for common workflow operations.
 */
public class WorkflowClient {
    
    private final WorkflowClientService clientService;
    
    /**
     * Creates a new WorkflowClient with the default configuration.
     */
    public WorkflowClient() {
        this.clientService = WorkflowClientBuilder.create().build();
    }
    
    /**
     * Creates a new WorkflowClient with the specified base URL.
     * 
     * @param baseUrl the base URL of the workflow API
     */
    public WorkflowClient(String baseUrl) {
        this.clientService = WorkflowClientBuilder.create()
                .baseUrl(baseUrl)
                .build();
    }
    
    /**
     * Creates a new WorkflowClient with the specified client service.
     * 
     * @param clientService the underlying client service implementation
     */
    public WorkflowClient(WorkflowClientService clientService) {
        this.clientService = clientService;
    }
    
    /**
     * Submits a workflow for execution.
     * 
     * @param workflow the workflow to execute
     * @return a Mono containing the submission result
     */
    public Mono<SubmitWorkflowOutput> submitWorkflow(Workflow workflow) {
        SubmitWorkflowInput input = new SubmitWorkflowInput(workflow);
        return clientService.submitWorkflow(input);
    }
    
    /**
     * Submits a workflow by ID for execution.
     * 
     * @param workflowId the ID of the workflow to execute
     * @return a Mono containing the submission result
     */
    public Mono<SubmitWorkflowByIdOutput> submitWorkflowById(String workflowId) {
        SubmitWorkflowByIdInput input = new SubmitWorkflowByIdInput(workflowId);
        return clientService.submitWorkflowById(input);
    }
    
    /**
     * Gets the status of a workflow execution.
     * 
     * @param runId the run ID to check
     * @return a Mono containing the workflow status result
     */
    public Mono<GetWorkflowStatusOutput> getWorkflowStatus(String runId) {
        GetWorkflowStatusInput input = new GetWorkflowStatusInput(runId);
        return clientService.getWorkflowStatus(input);
    }
    
    /**
     * Gets workflow data for a specific run.
     * 
     * @param runId the run ID to get data for
     * @return a Mono containing the workflow data result
     */
    public Mono<GetWorkflowDataOutput> getWorkflowData(String runId) {
        GetWorkflowDataInput input = new GetWorkflowDataInput(runId);
        return clientService.getWorkflowData(input);
    }
    
    /**
     * Lists all available workflows.
     * 
     * @return a Mono containing the list of workflows result
     */
    public Mono<ListWorkflowsOutput> listWorkflows() {
        ListWorkflowsInput input = new ListWorkflowsInput();
        return clientService.listWorkflows(input);
    }
    
    /**
     * Cancels a workflow execution.
     * 
     * @param runId the run ID to cancel
     * @return a Mono containing the cancellation result
     */
    public Mono<CancelWorkflowOutput> cancelWorkflow(String runId) {
        CancelWorkflowInput input = new CancelWorkflowInput(runId);
        return clientService.cancelWorkflow(input);
    }
    
    /**
     * Gets workflow definition by ID.
     * 
     * @param workflowId the workflow ID
     * @return a Mono containing the workflow definition result
     */
    public Mono<GetWorkflowDefinitionOutput> getWorkflowDefinition(String workflowId) {
        GetWorkflowDefinitionInput input = new GetWorkflowDefinitionInput(workflowId);
        return clientService.getWorkflowDefinition(input);
    }
    
    /**
     * Returns the underlying client service.
     * 
     * @return the WorkflowClientService instance
     */
    public WorkflowClientService getClientService() {
        return clientService;
    }
}
