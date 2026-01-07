package com.uwf.workflow.client.model;

import com.uwf.workflow.primitive.model.WorkflowContext;

/**
 * Output result from getting workflow status.
 */
public class GetWorkflowStatusOutput extends BaseOutput {
    
    private final WorkflowContext workflowContext;
    
    /**
     * Creates a successful GetWorkflowStatusOutput.
     * 
     * @param workflowContext the workflow context containing status information
     */
    public GetWorkflowStatusOutput(WorkflowContext workflowContext) {
        super(true, null);
        this.workflowContext = workflowContext;
    }
    
    /**
     * Creates a failed GetWorkflowStatusOutput.
     * 
     * @param errorMessage the error message
     */
    public GetWorkflowStatusOutput(String errorMessage) {
        super(false, errorMessage);
        this.workflowContext = null;
    }
    
    /**
     * Returns the workflow context containing status information.
     * 
     * @return the workflow context
     */
    public WorkflowContext getWorkflowContext() {
        return workflowContext;
    }
    
    /**
     * Creates a successful GetWorkflowStatusOutput.
     * 
     * @param workflowContext the workflow context
     * @return a successful GetWorkflowStatusOutput
     */
    public static GetWorkflowStatusOutput success(WorkflowContext workflowContext) {
        return new GetWorkflowStatusOutput(workflowContext);
    }
    
    /**
     * Creates a failed GetWorkflowStatusOutput.
     * 
     * @param errorMessage the error message
     * @return a failed GetWorkflowStatusOutput
     */
    public static GetWorkflowStatusOutput failure(String errorMessage) {
        return new GetWorkflowStatusOutput(errorMessage);
    }
    
    @Override
    public String toString() {
        if (isSuccess()) {
            return "GetWorkflowStatusOutput [success=true, workflowContext=" + 
                   (workflowContext != null ? workflowContext.runId() : "null") + "]";
        } else {
            return "GetWorkflowStatusOutput [success=false, errorMessage=" + getErrorMessage() + "]";
        }
    }
}
