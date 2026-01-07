package com.uwf.workflow.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for workflow client.
 * Centralizes all port and config related constants.
 */
@Configuration
@ConfigurationProperties(prefix = "workflow.client")
public class WorkflowClientConfig {
    
    /**
     * Base URL for the workflow API.
     */
    private String baseUrl = "http://localhost:8080";
    
    /**
     * Connection timeout in milliseconds.
     */
    private int timeout = 5000;
    
    /**
     * Retry configuration.
     */
    private RetryConfig retry = new RetryConfig();
    
    /**
     * API endpoint paths.
     */
    private ApiPaths apiPaths = new ApiPaths();
    
    /**
     * Default server port for the client application.
     */
    private int serverPort = 8082;
    
    public static class RetryConfig {
        private int maxAttempts = 3;
        private int backoffDelay = 1000;
        
        public int getMaxAttempts() {
            return maxAttempts;
        }
        
        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }
        
        public int getBackoffDelay() {
            return backoffDelay;
        }
        
        public void setBackoffDelay(int backoffDelay) {
            this.backoffDelay = backoffDelay;
        }
    }
    
    public static class ApiPaths {
        private String submitWorkflow = "/api/workflows/run";
        private String submitWorkflowById = "/api/workflows/run/{workflowId}";
        private String getWorkflowStatus = "/api/workflows/status/{runId}";
        private String getWorkflowData = "/api/workflows/data/{runId}";
        private String listWorkflows = "/api/workflows/list";
        private String cancelWorkflow = "/api/workflows/cancel/{runId}";
        private String getWorkflowDefinition = "/api/workflows/definition/{workflowId}";
        
        public String getSubmitWorkflow() {
            return submitWorkflow;
        }
        
        public void setSubmitWorkflow(String submitWorkflow) {
            this.submitWorkflow = submitWorkflow;
        }
        
        public String getSubmitWorkflowById() {
            return submitWorkflowById;
        }
        
        public void setSubmitWorkflowById(String submitWorkflowById) {
            this.submitWorkflowById = submitWorkflowById;
        }
        
        public String getGetWorkflowStatus() {
            return getWorkflowStatus;
        }
        
        public void setGetWorkflowStatus(String getWorkflowStatus) {
            this.getWorkflowStatus = getWorkflowStatus;
        }
        
        public String getGetWorkflowData() {
            return getWorkflowData;
        }
        
        public void setGetWorkflowData(String getWorkflowData) {
            this.getWorkflowData = getWorkflowData;
        }
        
        public String getListWorkflows() {
            return listWorkflows;
        }
        
        public void setListWorkflows(String listWorkflows) {
            this.listWorkflows = listWorkflows;
        }
        
        public String getCancelWorkflow() {
            return cancelWorkflow;
        }
        
        public void setCancelWorkflow(String cancelWorkflow) {
            this.cancelWorkflow = cancelWorkflow;
        }
        
        public String getGetWorkflowDefinition() {
            return getWorkflowDefinition;
        }
        
        public void setGetWorkflowDefinition(String getWorkflowDefinition) {
            this.getWorkflowDefinition = getWorkflowDefinition;
        }
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public RetryConfig getRetry() {
        return retry;
    }
    
    public void setRetry(RetryConfig retry) {
        this.retry = retry;
    }
    
    public ApiPaths getApiPaths() {
        return apiPaths;
    }
    
    public void setApiPaths(ApiPaths apiPaths) {
        this.apiPaths = apiPaths;
    }
    
    public int getServerPort() {
        return serverPort;
    }
    
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
