package com.uwf.workflow.client.model;

/**
 * Input parameters for listing workflows.
 * Currently empty as no parameters are needed for listing workflows.
 */
public class ListWorkflowsInput extends BaseInput {
    
    /**
     * Creates a new ListWorkflowsInput.
     */
    public ListWorkflowsInput() {
        // No parameters needed
    }
    
    @Override
    public String toString() {
        return "ListWorkflowsInput []";
    }
}
