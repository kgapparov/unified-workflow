package com.uwf.workflow.client.model;

import java.util.Map;

/**
 * Output result from getting workflow data.
 */
public class GetWorkflowDataOutput extends BaseOutput {
    
    private final Map<String, Object> workflowData;
    
    /**
     * Creates a successful GetWorkflowDataOutput.
     * 
     * @param workflowData the workflow data map
     */
    public GetWorkflowDataOutput(Map<String, Object> workflowData) {
        super(true, null);
        this.workflowData = workflowData;
    }
    
    /**
     * Creates a failed GetWorkflowDataOutput.
     * 
     * @param errorMessage the error message
     */
    public GetWorkflowDataOutput(String errorMessage) {
        super(false, errorMessage);
        this.workflowData = null;
    }
    
    /**
     * Returns the workflow data map.
     * 
     * @return the workflow data
     */
    public Map<String, Object> getWorkflowData() {
        return workflowData;
    }
    
    /**
     * Creates a successful GetWorkflowDataOutput.
     * 
     * @param workflowData the workflow data map
     * @return a successful GetWorkflowDataOutput
     */
    public static GetWorkflowDataOutput success(Map<String, Object> workflowData) {
        return new GetWorkflowDataOutput(workflowData);
    }
    
    /**
     * Creates a failed GetWorkflowDataOutput.
     * 
     * @param errorMessage the error message
     * @return a failed GetWorkflowDataOutput
     */
    public static GetWorkflowDataOutput failure(String errorMessage) {
        return new GetWorkflowDataOutput(errorMessage);
    }
    
    @Override
    public String toString() {
        if (isSuccess()) {
            return "GetWorkflowDataOutput [success=true, dataSize=" + 
                   (workflowData != null ? workflowData.size() : 0) + "]";
        } else {
            return "GetWorkflowDataOutput [success=false, errorMessage=" + getErrorMessage() + "]";
        }
    }
}
