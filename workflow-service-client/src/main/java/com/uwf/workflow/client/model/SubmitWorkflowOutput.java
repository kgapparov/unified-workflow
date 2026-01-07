package com.uwf.workflow.client.model;

/**
 * Output result from submitting a workflow for execution.
 */
public class SubmitWorkflowOutput extends BaseOutput {
    
    private final String runId;
    private final String workflowId;
    private final String message;
    
    /**
     * Creates a successful SubmitWorkflowOutput.
     * 
     * @param runId the run ID of the submitted workflow
     * @param workflowId the workflow ID
     * @param message a success message
     */
    public SubmitWorkflowOutput(String runId, String workflowId, String message) {
        super(true, null);
        this.runId = runId;
        this.workflowId = workflowId;
        this.message = message;
    }
    
    /**
     * Creates a failed SubmitWorkflowOutput.
     * 
     * @param errorMessage the error message
     */
    public SubmitWorkflowOutput(String errorMessage) {
        super(false, errorMessage);
        this.runId = null;
        this.workflowId = null;
        this.message = null;
    }
    
    /**
     * Returns the run ID of the submitted workflow.
     * 
     * @return the run ID
     */
    public String getRunId() {
        return runId;
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
     * Returns the success message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Creates a successful SubmitWorkflowOutput.
     * 
     * @param runId the run ID
     * @param workflowId the workflow ID
     * @param message the success message
     * @return a successful SubmitWorkflowOutput
     */
    public static SubmitWorkflowOutput success(String runId, String workflowId, String message) {
        return new SubmitWorkflowOutput(runId, workflowId, message);
    }
    
    /**
     * Creates a failed SubmitWorkflowOutput.
     * 
     * @param errorMessage the error message
     * @return a failed SubmitWorkflowOutput
     */
    public static SubmitWorkflowOutput failure(String errorMessage) {
        return new SubmitWorkflowOutput(errorMessage);
    }
    
    @Override
    public String toString() {
        if (isSuccess()) {
            return "SubmitWorkflowOutput [success=true, runId=" + runId + 
                   ", workflowId=" + workflowId + ", message=" + message + "]";
        } else {
            return "SubmitWorkflowOutput [success=false, errorMessage=" + getErrorMessage() + "]";
        }
    }
}
