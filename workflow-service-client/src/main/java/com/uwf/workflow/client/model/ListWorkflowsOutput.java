package com.uwf.workflow.client.model;

import java.util.List;
import java.util.Map;

/**
 * Output result from listing workflows.
 */
public class ListWorkflowsOutput extends BaseOutput {
    
    private final List<Map<String, String>> workflows;
    
    /**
     * Creates a successful ListWorkflowsOutput.
     * 
     * @param workflows the list of workflow information
     */
    public ListWorkflowsOutput(List<Map<String, String>> workflows) {
        super(true, null);
        this.workflows = workflows;
    }
    
    /**
     * Creates a failed ListWorkflowsOutput.
     * 
     * @param errorMessage the error message
     */
    public ListWorkflowsOutput(String errorMessage) {
        super(false, errorMessage);
        this.workflows = null;
    }
    
    /**
     * Returns the list of workflow information.
     * 
     * @return the list of workflows
     */
    public List<Map<String, String>> getWorkflows() {
        return workflows;
    }
    
    /**
     * Creates a successful ListWorkflowsOutput.
     * 
     * @param workflows the list of workflow information
     * @return a successful ListWorkflowsOutput
     */
    public static ListWorkflowsOutput success(List<Map<String, String>> workflows) {
        return new ListWorkflowsOutput(workflows);
    }
    
    /**
     * Creates a failed ListWorkflowsOutput.
     * 
     * @param errorMessage the error message
     * @return a failed ListWorkflowsOutput
     */
    public static ListWorkflowsOutput failure(String errorMessage) {
        return new ListWorkflowsOutput(errorMessage);
    }
    
    @Override
    public String toString() {
        if (isSuccess()) {
            return "ListWorkflowsOutput [success=true, workflowCount=" + 
                   (workflows != null ? workflows.size() : 0) + "]";
        } else {
            return "ListWorkflowsOutput [success=false, errorMessage=" + getErrorMessage() + "]";
        }
    }
}
