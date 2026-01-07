package com.uwf.workflow.client.model;

/**
 * Input parameters for getting workflow definition.
 */
public class GetWorkflowDefinitionInput extends BaseInput {
    
    private final String workflowId;
    
    /**
     * Creates a new GetWorkflowDefinitionInput with the specified workflow ID.
     * 
     * @param workflowId the workflow ID
     */
    public GetWorkflowDefinitionInput(String workflowId) {
        this.workflowId = workflowId;
    }
    
    /**
     * Returns the workflow ID.
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
        return "GetWorkflowDefinitionInput [workflowId=" + workflowId + "]";
    }
}
