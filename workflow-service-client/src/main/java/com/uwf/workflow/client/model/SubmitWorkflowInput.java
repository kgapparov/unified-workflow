package com.uwf.workflow.client.model;

import com.uwf.workflow.common.model.Workflow;

/**
 * Input parameters for submitting a workflow for execution.
 */
public class SubmitWorkflowInput extends BaseInput {
    
    private final Workflow workflow;
    
    /**
     * Creates a new SubmitWorkflowInput with the specified workflow.
     * 
     * @param workflow the workflow to execute
     */
    public SubmitWorkflowInput(Workflow workflow) {
        this.workflow = workflow;
    }
    
    /**
     * Returns the workflow to execute.
     * 
     * @return the workflow
     */
    public Workflow getWorkflow() {
        return workflow;
    }
    
    /**
     * Validates that the workflow is not null.
     * 
     * @throws IllegalArgumentException if workflow is null
     */
    @Override
    public void validate() {
        if (workflow == null) {
            throw new IllegalArgumentException("Workflow cannot be null");
        }
    }
    
    @Override
    public String toString() {
        return "SubmitWorkflowInput [workflow=" + (workflow != null ? workflow.getId() : "null") + "]";
    }
}
