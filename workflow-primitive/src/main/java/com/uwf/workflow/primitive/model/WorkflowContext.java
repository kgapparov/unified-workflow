package com.uwf.workflow.primitive.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable record representing workflow execution metadata.
 * Contains information about the current state of workflow execution.
 */
public record WorkflowContext(
        String runId,
        String workflowDefinitionId,
        WorkflowStatus status,
        int currentStepIndex,
        int currentChildStepIndex,
        Instant startTime,
        Instant endTime,
        String errorMessage,
        String lastAttemptedStep
) {

    /**
     * Factory method to create a new workflow execution context.
     *
     * @param workflowDefinitionId the ID of the workflow definition
     * @return a new WorkflowContext with initial state
     */
    public static WorkflowContext newExecution(String workflowDefinitionId) {
        return new WorkflowContext(
                UUID.randomUUID().toString(),
                workflowDefinitionId,
                WorkflowStatus.PENDING,
                -1,
                -1,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Creates a new context with updated status.
     *
     * @param newStatus the new status
     * @return a new WorkflowContext with updated status
     */
    public WorkflowContext withStatus(WorkflowStatus newStatus) {
        return new WorkflowContext(
                runId,
                workflowDefinitionId,
                newStatus,
                currentStepIndex,
                currentChildStepIndex,
                startTime,
                endTime,
                errorMessage,
                lastAttemptedStep
        );
    }

    /**
     * Creates a new context with updated step and child step indices.
     *
     * @param newStepIndex the new step index
     * @param newChildStepIndex the new child step index
     * @return a new WorkflowContext with updated indices
     */
    public WorkflowContext withIndices(int newStepIndex, int newChildStepIndex) {
        return new WorkflowContext(
                runId,
                workflowDefinitionId,
                status,
                newStepIndex,
                newChildStepIndex,
                startTime,
                endTime,
                errorMessage,
                lastAttemptedStep
        );
    }

    /**
     * Creates a new context with updated error message.
     *
     * @param newErrorMessage the new error message
     * @return a new WorkflowContext with updated error message
     */
    public WorkflowContext withErrorMessage(String newErrorMessage) {
        return new WorkflowContext(
                runId,
                workflowDefinitionId,
                status,
                currentStepIndex,
                currentChildStepIndex,
                startTime,
                endTime,
                newErrorMessage,
                lastAttemptedStep
        );
    }

    /**
     * Creates a new context with updated start time.
     *
     * @param newStartTime the new start time
     * @return a new WorkflowContext with updated start time
     */
    public WorkflowContext withStartTime(Instant newStartTime) {
        return new WorkflowContext(
                runId,
                workflowDefinitionId,
                status,
                currentStepIndex,
                currentChildStepIndex,
                newStartTime,
                endTime,
                errorMessage,
                lastAttemptedStep
        );
    }

    /**
     * Creates a new context with updated end time.
     *
     * @param newEndTime the new end time
     * @return a new WorkflowContext with updated end time
     */
    public WorkflowContext withEndTime(Instant newEndTime) {
        return new WorkflowContext(
                runId,
                workflowDefinitionId,
                status,
                currentStepIndex,
                currentChildStepIndex,
                startTime,
                newEndTime,
                errorMessage,
                lastAttemptedStep
        );
    }

    /**
     * Creates a new context with updated current step index.
     *
     * @param newStepIndex the new step index
     * @return a new WorkflowContext with updated step index
     */
    public WorkflowContext withCurrentStepIndex(int newStepIndex) {
        return new WorkflowContext(
                runId,
                workflowDefinitionId,
                status,
                newStepIndex,
                currentChildStepIndex,
                startTime,
                endTime,
                errorMessage,
                lastAttemptedStep
        );
    }

    /**
     * Creates a new context with updated current child step index.
     *
     * @param newChildStepIndex the new child step index
     * @return a new WorkflowContext with updated child step index
     */
    public WorkflowContext withCurrentChildStepIndex(int newChildStepIndex) {
        return new WorkflowContext(
                runId,
                workflowDefinitionId,
                status,
                currentStepIndex,
                newChildStepIndex,
                startTime,
                endTime,
                errorMessage,
                lastAttemptedStep
        );
    }

    /**
     * Creates a new context with updated last attempted step.
     *
     * @param newLastAttemptedStep the new last attempted step name
     * @return a new WorkflowContext with updated last attempted step
     */
    public WorkflowContext withLastAttemptedStep(String newLastAttemptedStep) {
        return new WorkflowContext(
                runId,
                workflowDefinitionId,
                status,
                currentStepIndex,
                currentChildStepIndex,
                startTime,
                endTime,
                errorMessage,
                newLastAttemptedStep
        );
    }
}
