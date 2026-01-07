package com.uwf.workflow.client.model;

import com.uwf.workflow.common.model.Workflow;

/**
 * Output result from getting workflow definition.
 */
public class GetWorkflowDefinitionOutput extends BaseOutput {
    
    private final Workflow workflow;
    
    /**
     * Creates a successful GetWorkflowDefinitionOutput.
     * 
     * @param workflow the workflow definition
     */
    public GetWorkflowDefinitionOutput(Workflow workflow) {
        super(true, null);
        this.workflow = workflow;
    }
    
    /**
     * Creates a failed GetWorkflowDefinitionOutput.
     * 
     * @param errorMessage the error message
     */
    public GetWorkflowDefinitionOutput(String errorMessage) {
        super(false, errorMessage);
        this.workflow = null;
    }
    
    /**
     * Returns the workflow definition.
     * 
     * @return the workflow
     */
    public Workflow getWorkflow() {
        return workflow;
    }
    
    /**
     * Creates a successful GetWorkflowDefinitionOutput.
     * 
     * @param workflow the workflow definition
     * @return a successful GetWorkflowDefinitionOutput
     */
    public static GetWorkflowDefinitionOutput success(Workflow workflow) {
        return new GetWorkflowDefinitionOutput(workflow);
    }
    
    /**
     * Creates a failed GetWorkflowDefinitionOutput.
     * 
     * @param errorMessage the error message
     * @return a failed GetWorkflowDefinitionOutput
     */
    public static GetWorkflowDefinitionOutput failure(String errorMessage) {
        return new GetWorkflowDefinitionOutput(errorMessage);
    }
    
    @Override
    public String toString() {
        if (isSuccess()) {
            return "GetWorkflowDefinitionOutput [success=true, workflow=" + 
                   (workflow != null ? workflow.getId() : "null") + "]";
        } else {
            return "GetWorkflowDefinitionOutput [success=false, errorMessage=" + getErrorMessage() + "]";
        }
    }
}
