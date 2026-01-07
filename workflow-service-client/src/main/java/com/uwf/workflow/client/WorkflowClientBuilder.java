package com.uwf.workflow.client;

import com.uwf.workflow.client.config.WorkflowClientConfig;
import com.uwf.workflow.client.model.*;

/**
 * Builder for creating WorkflowClientService instances.
 * Provides a fluent API for configuring and creating workflow clients.
 */
public class WorkflowClientBuilder {
    
    private String baseUrl = "http://localhost:8080";
    private ClientType clientType = ClientType.REST;
    
    private WorkflowClientBuilder() {
        // Private constructor, use create() method
    }
    
    /**
     * Creates a new WorkflowClientBuilder instance.
     * 
     * @return a new builder instance
     */
    public static WorkflowClientBuilder create() {
        return new WorkflowClientBuilder();
    }
    
    /**
     * Sets the base URL for the workflow API.
     * 
     * @param baseUrl the base URL
     * @return this builder for method chaining
     */
    public WorkflowClientBuilder baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
    
    /**
     * Sets the client type to use.
     * 
     * @param clientType the client type
     * @return this builder for method chaining
     */
    public WorkflowClientBuilder clientType(ClientType clientType) {
        this.clientType = clientType;
        return this;
    }
    
    /**
     * Builds and returns a WorkflowClientService instance.
     * 
     * @return a configured WorkflowClientService instance
     */
    public WorkflowClientService build() {
        switch (clientType) {
            case REST:
                return createRestClient();
            // Add other client types here as they are implemented
            // case GRPC:
            //     return createGrpcClient();
            default:
                throw new IllegalArgumentException("Unsupported client type: " + clientType);
        }
    }
    
    /**
     * Creates a REST-based workflow client.
     * 
     * @return a REST-based WorkflowClientService
     */
    private WorkflowClientService createRestClient() {
        // Create a simple REST client without Spring dependencies
        // Use configuration values
        WorkflowClientConfig config = new WorkflowClientConfig();
        config.setBaseUrl(baseUrl);
        // Set default API paths
        WorkflowClientConfig.ApiPaths apiPaths = new WorkflowClientConfig.ApiPaths();
        config.setApiPaths(apiPaths);
        
        return new SimpleRestWorkflowClient(config);
    }
    
    /**
     * Simple REST client implementation for use with the builder.
     * This avoids Spring dependency injection issues.
     */
    private static class SimpleRestWorkflowClient implements WorkflowClientService {
        private final org.springframework.web.reactive.function.client.WebClient webClient;
        private final WorkflowClientConfig config;
        
        public SimpleRestWorkflowClient(WorkflowClientConfig config) {
            this.config = config;
            this.webClient = org.springframework.web.reactive.function.client.WebClient.builder()
                    .baseUrl(config.getBaseUrl())
                    .defaultHeader("Content-Type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                    .build();
        }
        
        @Override
        public reactor.core.publisher.Mono<SubmitWorkflowOutput> submitWorkflow(SubmitWorkflowInput input) {
            // Simplified implementation - in a real scenario, this would make actual HTTP calls
            return reactor.core.publisher.Mono.just(SubmitWorkflowOutput.failure("Simple client - not implemented"));
        }
        
        @Override
        public reactor.core.publisher.Mono<SubmitWorkflowByIdOutput> submitWorkflowById(SubmitWorkflowByIdInput input) {
            return reactor.core.publisher.Mono.just(SubmitWorkflowByIdOutput.failure("Simple client - not implemented"));
        }
        
        @Override
        public reactor.core.publisher.Mono<GetWorkflowStatusOutput> getWorkflowStatus(GetWorkflowStatusInput input) {
            return reactor.core.publisher.Mono.just(GetWorkflowStatusOutput.failure("Simple client - not implemented"));
        }
        
        @Override
        public reactor.core.publisher.Mono<GetWorkflowDataOutput> getWorkflowData(GetWorkflowDataInput input) {
            return reactor.core.publisher.Mono.just(GetWorkflowDataOutput.failure("Simple client - not implemented"));
        }
        
        @Override
        public reactor.core.publisher.Mono<ListWorkflowsOutput> listWorkflows(ListWorkflowsInput input) {
            return reactor.core.publisher.Mono.just(ListWorkflowsOutput.failure("Simple client - not implemented"));
        }
        
        @Override
        public reactor.core.publisher.Mono<CancelWorkflowOutput> cancelWorkflow(CancelWorkflowInput input) {
            return reactor.core.publisher.Mono.just(CancelWorkflowOutput.failure("Simple client - not implemented"));
        }
        
        @Override
        public reactor.core.publisher.Mono<GetWorkflowDefinitionOutput> getWorkflowDefinition(GetWorkflowDefinitionInput input) {
            return reactor.core.publisher.Mono.just(GetWorkflowDefinitionOutput.failure("Simple client - not implemented"));
        }
        
        @Override
        public reactor.core.publisher.Flux<java.util.Map<String, Object>> getRunningWorkflows() {
            return reactor.core.publisher.Flux.empty();
        }
    }
    
    /**
     * Enum representing different client types.
     */
    public enum ClientType {
        /** REST API client */
        REST,
        /** gRPC client (future implementation) */
        GRPC,
        /** In-memory client for testing (future implementation) */
        IN_MEMORY
    }
}
