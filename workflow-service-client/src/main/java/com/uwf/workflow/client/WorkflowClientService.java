package com.uwf.workflow.client;

import com.uwf.workflow.common.model.Workflow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Service interface for interacting with workflow services.
 * Provides reactive methods for workflow operations.
 */
public interface WorkflowClientService {

    /**
     * Submit a workflow for execution.
     *
     * @param workflow the workflow to execute
     * @return a Mono containing the run ID
     */
    Mono<String> submitWorkflow(Workflow workflow);

    /**
     * Submit a workflow by ID for execution.
     *
     * @param workflowId the ID of the workflow to execute
     * @return a Mono containing the run ID
     */
    Mono<String> submitWorkflow(String workflowId);

    /**
     * Get the status of a workflow execution.
     *
     * @param runId the run ID to check
     * @return a Mono containing the workflow status
     */
    Mono<Map<String, Object>> getWorkflowStatus(String runId);

    /**
     * Get all running workflows.
     *
     * @return a Flux of workflow statuses
     */
    Flux<Map<String, Object>> getRunningWorkflows();

    /**
     * Cancel a workflow execution.
     *
     * @param runId the run ID to cancel
     * @return a Mono indicating success or failure
     */
    Mono<Boolean> cancelWorkflow(String runId);

    /**
     * Get workflow definition by ID.
     *
     * @param workflowId the workflow ID
     * @return a Mono containing the workflow definition
     */
    Mono<Workflow> getWorkflowDefinition(String workflowId);
}
