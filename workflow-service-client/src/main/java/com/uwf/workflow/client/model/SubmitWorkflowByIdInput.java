package com.uwf.workflow.client.model;

/**
 * Input parameters for submitting a workflow by ID for execution.
 */
public class SubmitWorkflowByIdInput extends BaseInput {
    
    private final String workflowId;
    
    /**
     * Creates a new SubmitWorkflowByIdInput with the specified workflow ID.
     * 
     * @param workflowId the ID of the workflow to execute
     */
    public SubmitWorkflowByIdInput(String workflowId) {
        this.workflowId = workflowId;
    }
    
    /**
     * Returns the workflow ID to execute.
     * 
     * @return the workflow ID
     */
    public String getWorkflowId() {
        return workflowId;
    }
    
    /**
     * Validates that the workflow ID is not null or empty.
     * 
     * @throws IllegalArgumentException if workflowId is null or empty
     */
    @Override
    public void validate() {
        if (workflowId == null || workflowId.trim().isEmpty()) {
            throw new IllegalArgumentException("Workflow ID cannot be null or empty");
        }
    }
    
    @Override
    public String toString() {
        return "SubmitWorkflowByIdInput [workflowId=" + workflowId + "]";
    }
}
