package com.uwf.workflow.client.model;

/**
 * Input parameters for getting workflow status.
 */
public class GetWorkflowStatusInput extends BaseInput {
    
    private final String runId;
    
    /**
     * Creates a new GetWorkflowStatusInput with the specified run ID.
     * 
     * @param runId the run ID to check
     */
    public GetWorkflowStatusInput(String runId) {
        this.runId = runId;
    }
    
    /**
     * Returns the run ID to check.
     * 
     * @return the run ID
     */
    public String getRunId() {
        return runId;
    }
    
    /**
     * Validates that the run ID is not null or empty.
     * 
     * @throws IllegalArgumentException if runId is null or empty
     */
    @Override
    public void validate() {
        if (runId == null || runId.trim().isEmpty()) {
            throw new IllegalArgumentException("Run ID cannot be null or empty");
        }
    }
    
    @Override
    public String toString() {
        return "GetWorkflowStatusInput [runId=" + runId + "]";
    }
}
