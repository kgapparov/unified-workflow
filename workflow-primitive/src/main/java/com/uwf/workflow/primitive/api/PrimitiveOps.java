package com.uwf.workflow.primitive.api;

import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;

import java.util.concurrent.CompletableFuture;

/**
 * Primitive operations interface.
 * Provides basic utilities needed by all tasks with async support.
 */
public interface PrimitiveOps {
    /**
     * Logs an informational message.
     */
    CompletableFuture<Void> logInfo(String message);

    /**
     * Logs a warning message.
     */
    CompletableFuture<Void> logWarning(String message);

    /**
     * Logs an error message.
     */
    CompletableFuture<Void> logError(String message);

    /**
     * Logs a debug message.
     */
    CompletableFuture<Void> logDebug(String message);

    /**
     * Gets the current timestamp in milliseconds.
     */
    CompletableFuture<Long> getCurrentTimestamp();

    /**
     * Sleeps for the specified number of milliseconds.
     */
    CompletableFuture<Void> sleep(long millis);

    /**
     * Generates a random UUID.
     */
    CompletableFuture<String> generateRandomId();

    /**
     * Validates that a condition is true.
     */
    CompletableFuture<Void> validate(boolean condition, String message);

    /**
     * Stores data in the workflow context.
     */
    CompletableFuture<Void> storeData(WorkflowContext context, String key, Object value);

    /**
     * Retrieves data from the workflow context.
     */
    <T> CompletableFuture<T> retrieveData(WorkflowContext context, String key, Class<T> type);
}
