package com.uwf.workflow.client;

import com.uwf.workflow.client.config.WorkflowClientConfig;
import com.uwf.workflow.client.model.*;
import com.uwf.workflow.common.model.Workflow;
import com.uwf.workflow.primitive.model.WorkflowContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * REST-based implementation of WorkflowClientService.
 * Communicates with the workflow API via HTTP REST calls.
 */
@Service
public class RestWorkflowClientService implements WorkflowClientService {

    private final WebClient webClient;
    private final WorkflowClientConfig config;
    
    /**
     * Creates a new RestWorkflowClientService with configuration.
     * 
     * @param config the workflow client configuration
     */
    public RestWorkflowClientService(WorkflowClientConfig config) {
        this.config = config;
        this.webClient = WebClient.builder()
                .baseUrl(config.getBaseUrl())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    @Override
    public Mono<SubmitWorkflowOutput> submitWorkflow(SubmitWorkflowInput input) {
        input.validate();
        
        return webClient.post()
                .uri(config.getApiPaths().getSubmitWorkflow())
                .bodyValue(input.getWorkflow())
                .retrieve()
                .onStatus(status -> status.isError(), response -> 
                    response.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RuntimeException("Failed to submit workflow: " + error)))
                )
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .map(response -> SubmitWorkflowOutput.success(
                    response.get("runId"),
                    response.get("workflowId"),
                    response.get("message")
                ))
                .onErrorResume(e -> Mono.just(SubmitWorkflowOutput.failure(e.getMessage())));
    }
    
    @Override
    public Mono<SubmitWorkflowByIdOutput> submitWorkflowById(SubmitWorkflowByIdInput input) {
        input.validate();
        
        return webClient.post()
                .uri(config.getApiPaths().getSubmitWorkflowById(), input.getWorkflowId())
                .retrieve()
                .onStatus(status -> status.isError(), response -> 
                    response.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RuntimeException("Failed to submit workflow by ID: " + error)))
                )
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .map(response -> SubmitWorkflowByIdOutput.success(
                    response.get("runId"),
                    response.get("workflowId"),
                    response.get("message")
                ))
                .onErrorResume(e -> Mono.just(SubmitWorkflowByIdOutput.failure(e.getMessage())));
    }
    
    @Override
    public Mono<GetWorkflowStatusOutput> getWorkflowStatus(GetWorkflowStatusInput input) {
        input.validate();
        
        return webClient.get()
                .uri(config.getApiPaths().getGetWorkflowStatus(), input.getRunId())
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, response -> 
                    Mono.error(new RuntimeException("Workflow not found for runId: " + input.getRunId()))
                )
                .onStatus(status -> status.isError(), response -> 
                    response.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RuntimeException("Failed to get workflow status: " + error)))
                )
                .bodyToMono(WorkflowContext.class)
                .map(GetWorkflowStatusOutput::success)
                .onErrorResume(e -> Mono.just(GetWorkflowStatusOutput.failure(e.getMessage())));
    }
    
    @Override
    public Mono<GetWorkflowDataOutput> getWorkflowData(GetWorkflowDataInput input) {
        input.validate();
        
        return webClient.get()
                .uri(config.getApiPaths().getGetWorkflowData(), input.getRunId())
                .retrieve()
                .onStatus(status -> status == HttpStatus.NOT_FOUND, response -> 
                    Mono.error(new RuntimeException("Workflow data not found for runId: " + input.getRunId()))
                )
                .onStatus(status -> status.isError(), response -> 
                    response.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RuntimeException("Failed to get workflow data: " + error)))
                )
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(GetWorkflowDataOutput::success)
                .onErrorResume(e -> Mono.just(GetWorkflowDataOutput.failure(e.getMessage())));
    }
    
    @Override
    public Mono<ListWorkflowsOutput> listWorkflows(ListWorkflowsInput input) {
        // No validation needed for empty input
        
        return webClient.get()
                .uri(config.getApiPaths().getListWorkflows())
                .retrieve()
                .onStatus(status -> status.isError(), response -> 
                    response.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RuntimeException("Failed to list workflows: " + error)))
                )
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, String>>>() {})
                .map(ListWorkflowsOutput::success)
                .onErrorResume(e -> Mono.just(ListWorkflowsOutput.failure(e.getMessage())));
    }
    
    @Override
    public Mono<CancelWorkflowOutput> cancelWorkflow(CancelWorkflowInput input) {
        input.validate();
        
        // Note: The cancel endpoint doesn't exist in the current controller
        // This is a placeholder for future implementation
        return webClient.post()
                .uri(config.getApiPaths().getCancelWorkflow(), input.getRunId())
                .retrieve()
                .onStatus(status -> status.isError(), response -> 
                    response.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RuntimeException("Failed to cancel workflow: " + error)))
                )
                .bodyToMono(new ParameterizedTypeReference<Map<String, Boolean>>() {})
                .map(response -> CancelWorkflowOutput.success(response.get("canceled")))
                .onErrorResume(e -> Mono.just(CancelWorkflowOutput.failure("Cancel workflow endpoint not implemented yet: " + e.getMessage())));
    }
    
    @Override
    public Mono<GetWorkflowDefinitionOutput> getWorkflowDefinition(GetWorkflowDefinitionInput input) {
        input.validate();
        
        // Note: The get workflow definition endpoint doesn't exist in the current controller
        // This is a placeholder for future implementation
        return webClient.get()
                .uri(config.getApiPaths().getGetWorkflowDefinition(), input.getWorkflowId())
                .retrieve()
                .onStatus(status -> status.isError(), response -> 
                    response.bodyToMono(String.class)
                            .flatMap(error -> Mono.error(new RuntimeException("Failed to get workflow definition: " + error)))
                )
                .bodyToMono(com.uwf.workflow.common.model.Workflow.class)
                .map(GetWorkflowDefinitionOutput::success)
                .onErrorResume(e -> Mono.just(GetWorkflowDefinitionOutput.failure("Get workflow definition endpoint not implemented yet: " + e.getMessage())));
    }
    
    @Override
    public Flux<Map<String, Object>> getRunningWorkflows() {
        // Legacy method - returns raw Flux
        // This would need a proper endpoint implementation
        return Flux.empty();
    }
}
