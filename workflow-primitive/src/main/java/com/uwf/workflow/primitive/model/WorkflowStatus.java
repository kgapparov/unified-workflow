package com.uwf.workflow.primitive.model;

/**
 * Enum representing the status of a workflow execution.
 */
public enum WorkflowStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    PAUSED,
    SKIPPED,
    CANCELLED;

    /**
     * Checks if the status is terminal (no further execution possible).
     *
     * @return true if the status is terminal, false otherwise
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == PAUSED || this == SKIPPED || this == CANCELLED;
    }
}
