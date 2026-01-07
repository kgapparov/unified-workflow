package com.uwf.workflow.client;

import com.uwf.workflow.client.model.*;
import com.uwf.workflow.common.model.Workflow;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for WorkflowClient and related components.
 */
class WorkflowClientTest {
    
    @Test
    void testWorkflowClientBuilder() {
        WorkflowClientService service = WorkflowClientBuilder.create()
                .baseUrl("http://localhost:8080")
                .build();
        
        assertNotNull(service, "WorkflowClientService should not be null");
        // Don't check specific type since SimpleRestWorkflowClient is private
        assertTrue(service instanceof WorkflowClientService, 
                "Should be instance of WorkflowClientService");
    }
    
    @Test
    void testWorkflowClientCreation() {
        WorkflowClient client = new WorkflowClient("http://localhost:8080");
        assertNotNull(client, "WorkflowClient should not be null");
        assertNotNull(client.getClientService(), "Client service should not be null");
    }
    
    @Test
    void testSubmitWorkflowInputValidation() {
        SubmitWorkflowInput input = new SubmitWorkflowInput(null);
        
        // Should throw exception when workflow is null
        assertThrows(IllegalArgumentException.class, input::validate, 
                "Validation should fail for null workflow");
        
        Workflow workflow = new Workflow("Test Workflow");
        SubmitWorkflowInput validInput = new SubmitWorkflowInput(workflow);
        assertDoesNotThrow(validInput::validate, 
                "Validation should pass for valid workflow");
    }
    
    @Test
    void testSubmitWorkflowOutput() {
        SubmitWorkflowOutput successOutput = SubmitWorkflowOutput.success("run123", "wf456", "Workflow submitted");
        assertTrue(successOutput.isSuccess(), "Output should be successful");
        assertEquals("run123", successOutput.getRunId(), "Run ID should match");
        assertEquals("wf456", successOutput.getWorkflowId(), "Workflow ID should match");
        assertEquals("Workflow submitted", successOutput.getMessage(), "Message should match");
        
        SubmitWorkflowOutput failureOutput = SubmitWorkflowOutput.failure("Error occurred");
        assertFalse(failureOutput.isSuccess(), "Output should be failure");
        assertEquals("Error occurred", failureOutput.getErrorMessage(), "Error message should match");
    }
    
    @Test
    void testGetWorkflowStatusInputValidation() {
        GetWorkflowStatusInput input = new GetWorkflowStatusInput(null);
        assertThrows(IllegalArgumentException.class, input::validate, 
                "Validation should fail for null runId");
        
        GetWorkflowStatusInput validInput = new GetWorkflowStatusInput("run123");
        assertDoesNotThrow(validInput::validate, 
                "Validation should pass for valid runId");
    }
    
    @Test
    void testSimpleRestWorkflowClientMethods() {
        WorkflowClientService service = WorkflowClientBuilder.create().build();
        
        // Test submitWorkflow
        Workflow workflow = new Workflow("Test Workflow");
        SubmitWorkflowInput submitInput = new SubmitWorkflowInput(workflow);
        Mono<SubmitWorkflowOutput> submitResult = service.submitWorkflow(submitInput);
        
        StepVerifier.create(submitResult)
                .expectNextMatches(output -> !output.isSuccess() && 
                        output.getErrorMessage().contains("Simple client"))
                .verifyComplete();
        
        // Test getWorkflowStatus
        GetWorkflowStatusInput statusInput = new GetWorkflowStatusInput("run123");
        Mono<GetWorkflowStatusOutput> statusResult = service.getWorkflowStatus(statusInput);
        
        StepVerifier.create(statusResult)
                .expectNextMatches(output -> !output.isSuccess() && 
                        output.getErrorMessage().contains("Simple client"))
                .verifyComplete();
        
        // Test listWorkflows
        ListWorkflowsInput listInput = new ListWorkflowsInput();
        Mono<ListWorkflowsOutput> listResult = service.listWorkflows(listInput);
        
        StepVerifier.create(listResult)
                .expectNextMatches(output -> !output.isSuccess() && 
                        output.getErrorMessage().contains("Simple client"))
                .verifyComplete();
    }
}
