package com.uwf.workflow.client.model;

/**
 * Output result from submitting a workflow by ID for execution.
 */
public class SubmitWorkflowByIdOutput extends BaseOutput {
    
    private final String runId;
    private final String workflowId;
    private final String message;
    
    /**
     * Creates a successful SubmitWorkflowByIdOutput.
     * 
     * @param runId the run ID of the submitted workflow
     * @param workflowId the workflow ID
     * @param message a success message
     */
    public SubmitWorkflowByIdOutput(String runId, String workflowId, String message) {
        super(true, null);
        this.runId = runId;
        this.workflowId = workflowId;
        this.message = message;
    }
    
    /**
     * Creates a failed SubmitWorkflowByIdOutput.
     * 
     * @param errorMessage the error message
     */
    public SubmitWorkflowByIdOutput(String errorMessage) {
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
     * Creates a successful SubmitWorkflowByIdOutput.
     * 
     * @param runId the run ID
     * @param workflowId the workflow ID
     * @param message the success message
     * @return a successful SubmitWorkflowByIdOutput
     */
    public static SubmitWorkflowByIdOutput success(String runId, String workflowId, String message) {
        return new SubmitWorkflowByIdOutput(runId, workflowId, message);
    }
    
    /**
     * Creates a failed SubmitWorkflowByIdOutput.
     * 
     * @param errorMessage the error message
     * @return a failed SubmitWorkflowByIdOutput
     */
    public static SubmitWorkflowByIdOutput failure(String errorMessage) {
        return new SubmitWorkflowByIdOutput(errorMessage);
    }
    
    @Override
    public String toString() {
        if (isSuccess()) {
            return "SubmitWorkflowByIdOutput [success=true, runId=" + runId + 
                   ", workflowId=" + workflowId + ", message=" + message + "]";
        } else {
            return "SubmitWorkflowByIdOutput [success=false, errorMessage=" + getErrorMessage() + "]";
        }
    }
}
