package com.uwf.workflow.client.model;

/**
 * Output result from canceling a workflow.
 */
public class CancelWorkflowOutput extends BaseOutput {
    
    private final boolean canceled;
    
    /**
     * Creates a successful CancelWorkflowOutput.
     * 
     * @param canceled whether the workflow was successfully canceled
     */
    public CancelWorkflowOutput(boolean canceled) {
        super(true, null);
        this.canceled = canceled;
    }
    
    /**
     * Creates a failed CancelWorkflowOutput.
     * 
     * @param errorMessage the error message
     */
    public CancelWorkflowOutput(String errorMessage) {
        super(false, errorMessage);
        this.canceled = false;
    }
    
    /**
     * Returns whether the workflow was successfully canceled.
     * 
     * @return true if canceled, false otherwise
     */
    public boolean isCanceled() {
        return canceled;
    }
    
    /**
     * Creates a successful CancelWorkflowOutput.
     * 
     * @param canceled whether the workflow was successfully canceled
     * @return a successful CancelWorkflowOutput
     */
    public static CancelWorkflowOutput success(boolean canceled) {
        return new CancelWorkflowOutput(canceled);
    }
    
    /**
     * Creates a failed CancelWorkflowOutput.
     * 
     * @param errorMessage the error message
     * @return a failed CancelWorkflowOutput
     */
    public static CancelWorkflowOutput failure(String errorMessage) {
        return new CancelWorkflowOutput(errorMessage);
    }
    
    @Override
    public String toString() {
        if (isSuccess()) {
            return "CancelWorkflowOutput [success=true, canceled=" + canceled + "]";
        } else {
            return "CancelWorkflowOutput [success=false, errorMessage=" + getErrorMessage() + "]";
        }
    }
}
