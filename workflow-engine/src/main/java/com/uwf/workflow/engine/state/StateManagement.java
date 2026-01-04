package com.uwf.workflow.engine.state;

import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;

/**
 * Interface for workflow state management implementations.
 * Provides abstraction for different state storage backends (in-memory, database, distributed cache).
 */
public interface StateManagement {

    /**
     * Saves the workflow context to the store.
     *
     * @param context the workflow context to save
     */
    void saveContext(WorkflowContext context);

    /**
     * Retrieves the workflow context for the given run ID.
     *
     * @param runId the workflow run ID
     * @return the workflow context, or null if not found
     */
    WorkflowContext getContext(String runId);

    /**
     * Saves the workflow data to the store.
     *
     * @param runId the workflow run ID
     * @param data the workflow data to save
     */
    void saveData(String runId, WorkflowData data);

    /**
     * Retrieves the workflow data for the given run ID.
     *
     * @param runId the workflow run ID
     * @return the workflow data, or null if not found
     */
    WorkflowData getData(String runId);

    /**
     * Removes all state (both context and data) for the given run ID.
     *
     * @param runId the workflow run ID
     */
    void removeState(String runId);

    /**
     * Checks if a workflow context exists for the given run ID.
     *
     * @param runId the workflow run ID
     * @return true if context exists, false otherwise
     */
    boolean containsContext(String runId);

    /**
     * Checks if workflow data exists for the given run ID.
     *
     * @param runId the workflow run ID
     * @return true if data exists, false otherwise
     */
    boolean containsData(String runId);

    /**
     * Acquires a lock for a workflow run to prevent concurrent modifications.
     * Used in distributed implementations to ensure consistency.
     *
     * @param runId the workflow run ID to lock
     * @param timeoutMs maximum time to wait for the lock in milliseconds
     * @return true if lock was acquired, false otherwise
     */
    default boolean acquireLock(String runId, long timeoutMs) {
        // Default implementation for single-node (always succeeds)
        return true;
    }

    /**
     * Releases a lock for a workflow run.
     *
     * @param runId the workflow run ID to unlock
     */
    default void releaseLock(String runId) {
        // Default implementation for single-node (no-op)
    }

    /**
     * Sets time-to-live for workflow state.
     * Used in implementations that support automatic expiration.
     *
     * @param runId the workflow run ID
     * @param ttlSeconds time to live in seconds
     */
    default void setTtl(String runId, long ttlSeconds) {
        // Default implementation (no-op)
    }
}
