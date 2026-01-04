package com.uwf.workflow.queue;

/**
 * Interface for workflow queue implementations.
 * Provides abstraction for different queue backends (in-memory, database, message brokers).
 */
public interface WorkflowQueue {

    /**
     * Enqueues a workflow run ID for processing.
     *
     * @param runId the workflow run ID to enqueue
     */
    void enqueue(String runId);

    /**
     * Dequeues a workflow run ID for processing.
     *
     * @return the next run ID to process, or null if the queue is empty
     */
    String dequeue();

    /**
     * Acknowledges successful processing of a message.
     * Used in message broker implementations with explicit acknowledgement.
     *
     * @param messageId the message identifier to acknowledge
     */
    default void acknowledge(String messageId) {
        // Default implementation for in-memory queues (no-op)
    }

    /**
     * Rejects a message and schedules it for retry.
     * Used in message broker implementations with retry logic.
     *
     * @param messageId the message identifier to reject
     * @param delayMs delay in milliseconds before retry
     */
    default void reject(String messageId, long delayMs) {
        // Default implementation for in-memory queues (no-op)
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Gets the current size of the queue.
     *
     * @return the number of run IDs in the queue
     */
    int size();

    /**
     * Checks if a specific run ID is in the queue.
     *
     * @param runId the run ID to check
     * @return true if the run ID is in the queue, false otherwise
     */
    boolean contains(String runId);

    /**
     * Removes a specific run ID from the queue.
     *
     * @param runId the run ID to remove
     * @return true if the run ID was removed, false if it wasn't in the queue
     */
    boolean remove(String runId);

    /**
     * Disables processing of new workflow executions.
     * Existing workflows will continue to be processed.
     */
    void disableProcessing();

    /**
     * Enables processing of new workflow executions.
     */
    void enableProcessing();

    /**
     * Checks if processing of new workflow executions is enabled.
     *
     * @return true if processing is enabled, false otherwise
     */
    boolean isProcessingEnabled();

    /**
     * Clears all run IDs from the queue.
     */
    void clear();
}
