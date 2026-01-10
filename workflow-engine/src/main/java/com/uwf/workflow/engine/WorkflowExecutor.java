package com.uwf.workflow.engine;

import com.uwf.workflow.common.model.Step;
import com.uwf.workflow.common.model.Workflow;
import com.uwf.workflow.engine.state.StateManagement;
import com.uwf.workflow.primitive.api.Primitives;
import com.uwf.workflow.queue.WorkflowQueue;
import com.uwf.workflow.registry.WorkflowRegistry;
import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;
import com.uwf.workflow.primitive.model.WorkflowStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Core orchestration service for executing workflows asynchronously.
 * Simplified version that provides basic workflow execution functionality.
 */
@Service
public class WorkflowExecutor {

    private final StateManagement stateManagement;
    private final WorkflowQueue workflowQueue;
    private final WorkflowRegistry workflowRegistry;
    private final Primitives primitives;
    
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    public WorkflowExecutor(StateManagement stateManagement,
                           WorkflowQueue workflowQueue,
                           WorkflowRegistry workflowRegistry,
                           Primitives primitives) {
        this.stateManagement = stateManagement;
        this.workflowQueue = workflowQueue;
        this.workflowRegistry = workflowRegistry;
        this.primitives = primitives;
    }

    /**
     * Submits a workflow for execution and returns a run ID.
     *
     * @param workflow the workflow to execute
     * @return the unique run ID for this execution
     */
    public String submitWorkflow(Workflow workflow) {
        String runId = UUID.randomUUID().toString();
        
        // Create initial context
        WorkflowContext initialContext = new WorkflowContext(
                runId,
                workflow.getId(),
                WorkflowStatus.PENDING,
                -1,
                -1,
                null,
                null,
                null,
                null
        );
        
        WorkflowData initialData = new WorkflowData();

        // Inject primitives into the workflow and all its steps
        workflow.setPrimitives(primitives);

        // Register workflow if not already registered
        if (!workflowRegistry.containsWorkflow(workflow.getId())) {
            workflowRegistry.registerWorkflow(workflow);
        }

        stateManagement.saveContext(initialContext);
        stateManagement.saveData(runId, initialData);
        workflowQueue.enqueue(runId);

        // Start processing
        executorService.submit(() -> processWorkflow(runId));

        return runId;
    }

    /**
     * Submits a workflow by ID for execution and returns a run ID.
     *
     * @param workflowId the ID of the workflow to execute
     * @return the unique run ID for this execution
     * @throws IllegalArgumentException if the workflow is not found
     */
    public String submitWorkflow(String workflowId) {
        Workflow workflow = workflowRegistry.getWorkflow(workflowId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found: " + workflowId));
        return submitWorkflow(workflow);
    }

    /**
     * Processes a workflow from the queue.
     */
    private void processWorkflow(String runId) {
        WorkflowContext context = stateManagement.getContext(runId);
        WorkflowData data = stateManagement.getData(runId);

        if (context == null || data == null) {
            return;
        }

        if (context.status().isTerminal()) {
            return;
        }

        try {
            Workflow workflow = workflowRegistry.getWorkflow(context.workflowDefinitionId())
                    .orElseThrow(() -> new IllegalArgumentException("Workflow not found: " + context.workflowDefinitionId()));

            executeWorkflow(runId, context, data, workflow);
        } catch (Exception e) {
            WorkflowContext failedContext = context.withStatus(WorkflowStatus.FAILED)
                    .withErrorMessage("Execution error: " + e.getMessage())
                    .withEndTime(Instant.now());
            stateManagement.saveContext(failedContext);
        }
    }

    /**
     * Executes a workflow.
     */
    private void executeWorkflow(String runId, WorkflowContext context, WorkflowData data, Workflow workflow) throws Exception {
        WorkflowContext runningContext = context.withStatus(WorkflowStatus.RUNNING).withStartTime(Instant.now());
        stateManagement.saveContext(runningContext);

        // Store workflow metrics
        storeWorkflowMetrics(workflow, data, runningContext.startTime(), null);

        try {
            // Execute all steps in the workflow
            for (int stepIndex = 0; stepIndex < workflow.getStepCount(); stepIndex++) {
                Step step = workflow.getStep(stepIndex);
                
                // Update context with current step
                WorkflowContext stepContext = runningContext
                        .withCurrentStepIndex(stepIndex)
                        .withLastAttemptedStep(step.getName());
                stateManagement.saveContext(stepContext);

                // Execute the step
                step.run(stepContext, data);
                
                // Save updated data
                stateManagement.saveData(runId, data);
            }

            // Workflow completed successfully
            Instant endTime = Instant.now();
            WorkflowContext completedContext = runningContext
                    .withStatus(WorkflowStatus.COMPLETED)
                    .withEndTime(endTime);
            stateManagement.saveContext(completedContext);
            
            // Update workflow metrics with completion time
            storeWorkflowMetrics(workflow, data, runningContext.startTime(), endTime);

        } catch (Exception e) {
            // Workflow failed
            Instant endTime = Instant.now();
            WorkflowContext failedContext = runningContext
                    .withStatus(WorkflowStatus.FAILED)
                    .withErrorMessage("Step execution failed: " + e.getMessage())
                    .withEndTime(endTime);
            stateManagement.saveContext(failedContext);
            
            // Update workflow metrics with failure time
            storeWorkflowMetrics(workflow, data, runningContext.startTime(), endTime);
            throw e;
        }
    }

    /**
     * Stores workflow execution metrics in workflow data.
     *
     * @param workflow the workflow being executed
     * @param data the workflow data
     * @param startTime the workflow start time
     * @param endTime the workflow end time, or null if not completed
     */
    private void storeWorkflowMetrics(Workflow workflow, WorkflowData data, Instant startTime, Instant endTime) {
        String workflowMetricsKey = "workflow_metrics";
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("workflowId", workflow.getId());
        metrics.put("workflowDescription", workflow.getDescription());
        metrics.put("stepCount", workflow.getStepCount());
        metrics.put("totalChildStepCount", workflow.getTotalChildStepCount());
        metrics.put("startTime", startTime);
        metrics.put("endTime", endTime);
        if (startTime != null && endTime != null) {
            metrics.put("durationMillis", java.time.Duration.between(startTime, endTime).toMillis());
        }
        data.put(workflowMetricsKey, metrics);
    }

    /**
     * Gets the state management component for direct data manipulation.
     *
     * @return the state management component
     */
    public StateManagement getStateManagement() {
        return stateManagement;
    }

    /**
     * Shuts down the executor service gracefully.
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
