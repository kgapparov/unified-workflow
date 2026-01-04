package com.uwf.workflow.registry;

import com.uwf.workflow.common.model.Workflow;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the workflow registry.
 * Uses ConcurrentHashMap for thread-safe operations.
 */
@Component
public class InMemoryWorkflowRegistry implements WorkflowRegistry {

    private final Map<String, Workflow> workflows = new ConcurrentHashMap<>();

    /**
     * Registers a workflow with the registry.
     *
     * @param workflow the workflow to register
     */
    @Override
    public void registerWorkflow(Workflow workflow) {
        workflows.put(workflow.getId(), workflow);
    }

    /**
     * Retrieves a workflow by its ID.
     *
     * @param workflowId the workflow ID to retrieve
     * @return an Optional containing the workflow if found, empty otherwise
     */
    @Override
    public Optional<Workflow> getWorkflow(String workflowId) {
        return Optional.ofNullable(workflows.get(workflowId));
    }

    /**
     * Checks if a workflow exists in the registry.
     *
     * @param workflowId the workflow ID to check
     * @return true if the workflow exists, false otherwise
     */
    @Override
    public boolean containsWorkflow(String workflowId) {
        return workflows.containsKey(workflowId);
    }

    /**
     * Removes a workflow from the registry.
     *
     * @param workflowId the workflow ID to remove
     * @return true if the workflow was removed, false if it didn't exist
     */
    @Override
    public boolean removeWorkflow(String workflowId) {
        return workflows.remove(workflowId) != null;
    }

    /**
     * Gets all registered workflow IDs.
     *
     * @return an array of all workflow IDs
     */
    @Override
    public String[] getAllWorkflowIds() {
        return workflows.keySet().toArray(new String[0]);
    }

    /**
     * Gets the number of registered workflows.
     *
     * @return the count of registered workflows
     */
    @Override
    public int getWorkflowCount() {
        return workflows.size();
    }

    /**
     * Clears all workflows from the registry.
     */
    @Override
    public void clear() {
        workflows.clear();
    }
}
