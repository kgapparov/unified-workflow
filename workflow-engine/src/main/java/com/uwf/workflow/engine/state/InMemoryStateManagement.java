package com.uwf.workflow.engine.state;

import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Component for managing workflow state in memory using ConcurrentHashMap.
 * Provides thread-safe operations for storing and retrieving workflow context and data.
 */
@Component
public class InMemoryStateManagement implements StateManagement {

    private final ConcurrentMap<String, WorkflowContext> contextStore = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, WorkflowData> dataStore = new ConcurrentHashMap<>();

    /**
     * Saves the workflow context to the store.
     *
     * @param context the workflow context to save
     */
    public void saveContext(WorkflowContext context) {
        contextStore.put(context.runId(), context);
    }

    /**
     * Retrieves the workflow context for the given run ID.
     *
     * @param runId the workflow run ID
     * @return the workflow context, or null if not found
     */
    public WorkflowContext getContext(String runId) {
        return contextStore.get(runId);
    }

    /**
     * Saves the workflow data to the store.
     *
     * @param runId the workflow run ID
     * @param data the workflow data to save
     */
    public void saveData(String runId, WorkflowData data) {
        dataStore.put(runId, data);
    }

    /**
     * Retrieves the workflow data for the given run ID.
     *
     * @param runId the workflow run ID
     * @return the workflow data, or null if not found
     */
    public WorkflowData getData(String runId) {
        return dataStore.get(runId);
    }

    /**
     * Removes all state (both context and data) for the given run ID.
     *
     * @param runId the workflow run ID
     */
    public void removeState(String runId) {
        contextStore.remove(runId);
        dataStore.remove(runId);
    }

    /**
     * Checks if a workflow context exists for the given run ID.
     *
     * @param runId the workflow run ID
     * @return true if context exists, false otherwise
     */
    public boolean containsContext(String runId) {
        return contextStore.containsKey(runId);
    }

    /**
     * Checks if workflow data exists for the given run ID.
     *
     * @param runId the workflow run ID
     * @return true if data exists, false otherwise
     */
    public boolean containsData(String runId) {
        return dataStore.containsKey(runId);
    }
}
