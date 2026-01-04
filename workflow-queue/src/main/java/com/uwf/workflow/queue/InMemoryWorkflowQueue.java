package com.uwf.workflow.queue;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Component for managing a queue of workflow run IDs that need to be processed.
 * Uses ConcurrentLinkedQueue for thread-safe operations.
 */
@Component
public class InMemoryWorkflowQueue implements WorkflowQueue {

    private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean processingEnabled = new AtomicBoolean(true);

    /**
     * Enqueues a run ID for processing.
     *
     * @param runId the workflow run ID to enqueue
     */
    public void enqueue(String runId) {
        queue.offer(runId);
    }

    /**
     * Dequeues a run ID for processing.
     *
     * @return the next run ID to process, or null if the queue is empty
     */
    public String dequeue() {
        return queue.poll();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Gets the current size of the queue.
     *
     * @return the number of run IDs in the queue
     */
    public int size() {
        return queue.size();
    }

    /**
     * Checks if a specific run ID is in the queue.
     *
     * @param runId the run ID to check
     * @return true if the run ID is in the queue, false otherwise
     */
    public boolean contains(String runId) {
        return queue.contains(runId);
    }

    /**
     * Removes a specific run ID from the queue.
     *
     * @param runId the run ID to remove
     * @return true if the run ID was removed, false if it wasn't in the queue
     */
    public boolean remove(String runId) {
        return queue.remove(runId);
    }

    /**
     * Disables processing of new workflow executions.
     * Existing workflows will continue to be processed.
     */
    public void disableProcessing() {
        processingEnabled.set(false);
    }

    /**
     * Enables processing of new workflow executions.
     */
    public void enableProcessing() {
        processingEnabled.set(true);
    }

    /**
     * Checks if processing of new workflow executions is enabled.
     *
     * @return true if processing is enabled, false otherwise
     */
    public boolean isProcessingEnabled() {
        return processingEnabled.get();
    }

    /**
     * Clears all run IDs from the queue.
     */
    public void clear() {
        queue.clear();
    }
}
