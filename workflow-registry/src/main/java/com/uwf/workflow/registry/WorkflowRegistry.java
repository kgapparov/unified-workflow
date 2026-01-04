package com.uwf.workflow.registry;

import com.uwf.workflow.common.model.Workflow;

import java.util.Optional;

/**
 * Interface for workflow registry implementations.
 * Provides abstraction for different storage backends (in-memory, database, etc.).
 */
public interface WorkflowRegistry {

    /**
     * Registers a workflow with the registry.
     *
     * @param workflow the workflow to register
     */
    void registerWorkflow(Workflow workflow);

    /**
     * Retrieves a workflow by its ID.
     *
     * @param workflowId the workflow ID to retrieve
     * @return an Optional containing the workflow if found, empty otherwise
     */
    Optional<Workflow> getWorkflow(String workflowId);

    /**
     * Checks if a workflow exists in the registry.
     *
     * @param workflowId the workflow ID to check
     * @return true if the workflow exists, false otherwise
     */
    boolean containsWorkflow(String workflowId);

    /**
     * Removes a workflow from the registry.
     *
     * @param workflowId the workflow ID to remove
     * @return true if the workflow was removed, false if it didn't exist
     */
    boolean removeWorkflow(String workflowId);

    /**
     * Gets all registered workflow IDs.
     *
     * @return an array of all workflow IDs
     */
    String[] getAllWorkflowIds();

    /**
     * Gets the number of registered workflows.
     *
     * @return the count of registered workflows
     */
    int getWorkflowCount();

    /**
     * Clears all workflows from the registry.
     */
    void clear();

    /**
     * Initializes the registry (e.g., creates tables, loads data).
     * Called on application startup.
     */
    default void initialize() {
        // Default implementation does nothing
    }

    /**
     * Shuts down the registry (e.g., closes connections).
     * Called on application shutdown.
     */
    default void shutdown() {
        // Default implementation does nothing
    }
}
