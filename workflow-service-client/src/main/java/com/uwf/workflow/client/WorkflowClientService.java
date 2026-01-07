package com.uwf.workflow.client;

import com.uwf.workflow.client.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service interface for interacting with workflow services.
 * Provides reactive methods for workflow operations using structured input/output.
 */
public interface WorkflowClientService {

    /**
     * Submit a workflow for execution.
     *
     * @param input the input containing workflow to execute
     * @return a Mono containing the submission result
     */
    Mono<SubmitWorkflowOutput> submitWorkflow(SubmitWorkflowInput input);

    /**
     * Submit a workflow by ID for execution.
     *
     * @param input the input containing workflow ID to execute
     * @return a Mono containing the submission result
     */
    Mono<SubmitWorkflowByIdOutput> submitWorkflowById(SubmitWorkflowByIdInput input);

    /**
     * Get the status of a workflow execution.
     *
     * @param input the input containing run ID to check
     * @return a Mono containing the workflow status result
     */
    Mono<GetWorkflowStatusOutput> getWorkflowStatus(GetWorkflowStatusInput input);

    /**
     * Get workflow data for a specific run.
     *
     * @param input the input containing run ID to get data for
     * @return a Mono containing the workflow data result
     */
    Mono<GetWorkflowDataOutput> getWorkflowData(GetWorkflowDataInput input);

    /**
     * List all available workflows.
     *
     * @param input the input parameters (currently empty)
     * @return a Mono containing the list of workflows result
     */
    Mono<ListWorkflowsOutput> listWorkflows(ListWorkflowsInput input);

    /**
     * Cancel a workflow execution.
     *
     * @param input the input containing run ID to cancel
     * @return a Mono containing the cancellation result
     */
    Mono<CancelWorkflowOutput> cancelWorkflow(CancelWorkflowInput input);

    /**
     * Get workflow definition by ID.
     *
     * @param input the input containing workflow ID
     * @return a Mono containing the workflow definition result
     */
    Mono<GetWorkflowDefinitionOutput> getWorkflowDefinition(GetWorkflowDefinitionInput input);

    /**
     * Get all running workflows (legacy method for backward compatibility).
     * Note: This method returns raw Flux for compatibility.
     *
     * @return a Flux of workflow statuses
     */
    Flux<java.util.Map<String, Object>> getRunningWorkflows();
}
