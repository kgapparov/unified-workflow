package com.uwf.workflow.common.model;

import com.uwf.workflow.primitive.api.Primitives;
import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a logical grouping of operations (a "Branch") that manages
 * the flow control (looping through children, handling their requests/responses).
 * This is the logic engine that coordinates ChildStep execution.
 */
public abstract class Step {
    private final String name;
    private final List<ChildStep> childSteps;
    private final boolean parallel;
    private Primitives primitives;
    private Instant startTime;
    private Instant endTime;
    private WorkflowContext contextBefore;
    private WorkflowContext contextAfter;
    private WorkflowData dataBefore;
    private WorkflowData dataAfter;

    /**
     * Constructs a new step with the specified name.
     * Child steps will be executed sequentially by default.
     *
     * @param name the name of the step
     */
    protected Step(String name) {
        this(name, false);
    }
    
    /**
     * Constructs a new step with the specified name and parallel flag.
     *
     * @param name the name of the step
     * @param parallel whether child steps in this step should be executed in parallel
     */
    protected Step(String name, boolean parallel) {
        this.name = name;
        this.childSteps = new ArrayList<>();
        this.parallel = parallel;
    }

    /**
     * Constructs a new step with the specified name and child steps.
     * Child steps will be executed sequentially by default.
     *
     * @param name  the name of the step
     * @param childSteps the list of child steps in this step
     */
    protected Step(String name, List<ChildStep> childSteps) {
        this(name, childSteps, false);
    }
    
    /**
     * Constructs a new step with the specified name, child steps, and parallel flag.
     *
     * @param name  the name of the step
     * @param childSteps the list of child steps in this step
     * @param parallel whether child steps in this step should be executed in parallel
     */
    protected Step(String name, List<ChildStep> childSteps, boolean parallel) {
        this.name = name;
        this.childSteps = new ArrayList<>(childSteps);
        this.parallel = parallel;
    }

    /**
     * Gets the name of this step.
     *
     * @return the step name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets an unmodifiable view of the child steps in this step.
     *
     * @return the list of child steps
     */
    public List<ChildStep> getChildSteps() {
        return Collections.unmodifiableList(childSteps);
    }
    
    /**
     * Checks if child steps in this step should be executed in parallel.
     *
     * @return true if child steps should be executed in parallel, false for sequential execution
     */
    public boolean isParallel() {
        return parallel;
    }

    /**
     * Injects primitives into this step.
     *
     * @param primitives the primitives registry to inject
     */
    public void setPrimitives(Primitives primitives) {
        this.primitives = primitives;
    }

    /**
     * Gets the primitives registry.
     *
     * @return the primitives registry
     */
    public Primitives getPrimitives() {
        return primitives;
    }

    /**
     * Gets the start time of this step execution.
     *
     * @return the start time, or null if not started
     */
    public Instant getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time of this step execution.
     *
     * @return the end time, or null if not completed
     */
    public Instant getEndTime() {
        return endTime;
    }

    /**
     * Gets the duration of this step execution in milliseconds.
     *
     * @return the duration in milliseconds, or null if not completed
     */
    public Long getDurationMillis() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toMillis();
        }
        return null;
    }

    /**
     * Gets the context before step execution.
     *
     * @return the context before execution, or null if not started
     */
    public WorkflowContext getContextBefore() {
        return contextBefore;
    }

    /**
     * Gets the context after step execution.
     *
     * @return the context after execution, or null if not completed
     */
    public WorkflowContext getContextAfter() {
        return contextAfter;
    }

    /**
     * Gets the data before step execution (deep copy).
     *
     * @return the data before execution, or null if not started
     */
    public WorkflowData getDataBefore() {
        return dataBefore;
    }

    /**
     * Gets the data after step execution (deep copy).
     *
     * @return the data after execution, or null if not completed
     */
    public WorkflowData getDataAfter() {
        return dataAfter;
    }

    /**
     * Adds a child step to this step.
     *
     * @param childStep the child step to add
     * @return this step for method chaining
     */
    public Step addChildStep(ChildStep childStep) {
        childSteps.add(childStep);
        return this;
    }

    /**
     * Adds multiple child steps to this step.
     *
     * @param childStepsToAdd the child steps to add
     * @return this step for method chaining
     */
    public Step addChildSteps(List<ChildStep> childStepsToAdd) {
        childSteps.addAll(childStepsToAdd);
        return this;
    }

    /**
     * Gets the number of child steps in this step.
     *
     * @return the number of child steps
     */
    public int getChildStepCount() {
        return childSteps.size();
    }

    /**
     * Gets a child step by its index.
     *
     * @param index the index of the child step
     * @return the child step at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public ChildStep getChildStep(int index) {
        return childSteps.get(index);
    }

    /**
     * Checks if this step contains any child steps.
     *
     * @return true if the step has child steps, false otherwise
     */
    public boolean hasChildSteps() {
        return !childSteps.isEmpty();
    }

    /**
     * Runs the step with the provided context and data.
     * This is the logic engine that coordinates the request/response/validate lifecycle
     * for all child steps. Must be implemented by concrete Step classes.
     *
     * @param context the workflow execution context
     * @param data the shared workflow data
     * @throws Exception if the step execution fails
     */
    public abstract void run(WorkflowContext context, WorkflowData data) throws Exception;

    /**
     * Template method that concrete Step implementations can call to execute with timing.
     * This method sets start/end times, captures context/data before/after, and stores metrics.
     *
     * @param context the workflow execution context
     * @param data the shared workflow data
     * @param stepLogic the step logic to execute
     * @throws Exception if the step execution fails
     */
    protected void executeWithTiming(WorkflowContext context, WorkflowData data, StepLogic stepLogic) throws Exception {
        // Capture state before execution
        startTime = Instant.now();
        contextBefore = context;
        dataBefore = data.deepCopy();
        
        try {
            stepLogic.execute();
            endTime = Instant.now();
            // Capture state after successful execution
            contextAfter = context;
            dataAfter = data.deepCopy();
            storeStepMetrics(context, data);
        } catch (Exception e) {
            endTime = Instant.now();
            // Capture state after failed execution
            contextAfter = context;
            dataAfter = data.deepCopy();
            storeStepMetrics(context, data);
            throw e;
        }
    }

    /**
     * Stores step execution metrics in workflow data.
     * Includes context/data diffs for tracking changes.
     *
     * @param context the workflow execution context
     * @param data the shared workflow data
     */
    protected void storeStepMetrics(WorkflowContext context, WorkflowData data) {
        String stepKey = "step_" + getName() + "_metrics";
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("stepName", getName());
        metrics.put("startTime", startTime);
        metrics.put("endTime", endTime);
        if (startTime != null && endTime != null) {
            metrics.put("durationMillis", java.time.Duration.between(startTime, endTime).toMillis());
        }
        metrics.put("childStepCount", getChildStepCount());
        metrics.put("parallel", isParallel());
        
        // Store context before/after for diff tracking
        if (contextBefore != null) {
            metrics.put("contextBefore", contextBefore);
        }
        if (contextAfter != null) {
            metrics.put("contextAfter", contextAfter);
        }
        
        // Store data diffs
        if (dataBefore != null && dataAfter != null) {
            Map<String, Object> dataDiff = calculateDataDiff(dataBefore, dataAfter);
            metrics.put("dataDiff", dataDiff);
        }
        
        data.put(stepKey, metrics);
    }

    /**
     * Calculates the difference between two WorkflowData instances.
     * Returns a map showing added, modified, and removed keys.
     *
     * @param before the data before execution
     * @param after the data after execution
     * @return a map describing the changes
     */
    private Map<String, Object> calculateDataDiff(WorkflowData before, WorkflowData after) {
        Map<String, Object> diff = new HashMap<>();
        Map<String, Object> beforeMap = before.toMap();
        Map<String, Object> afterMap = after.toMap();
        
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(beforeMap.keySet());
        allKeys.addAll(afterMap.keySet());
        
        Map<String, Object> added = new HashMap<>();
        Map<String, Object> modified = new HashMap<>();
        Map<String, Object> removed = new HashMap<>();
        
        for (String key : allKeys) {
            Object beforeValue = beforeMap.get(key);
            Object afterValue = afterMap.get(key);
            
            if (beforeValue == null && afterValue != null) {
                added.put(key, afterValue);
            } else if (beforeValue != null && afterValue == null) {
                removed.put(key, beforeValue);
            } else if (!Objects.equals(beforeValue, afterValue)) {
                Map<String, Object> change = new HashMap<>();
                change.put("before", beforeValue);
                change.put("after", afterValue);
                modified.put(key, change);
            }
        }
        
        diff.put("added", added);
        diff.put("modified", modified);
        diff.put("removed", removed);
        diff.put("totalChanges", added.size() + modified.size() + removed.size());
        
        return diff;
    }

    /**
     * Functional interface for step execution logic.
     */
    @FunctionalInterface
    public interface StepLogic {
        void execute() throws Exception;
    }

    /**
     * Executes a child step using the standard request/response/validate pattern.
     * This is a helper method that concrete Step implementations can use.
     *
     * @param childStep the child step to execute
     * @param context the workflow execution context
     * @param data the shared workflow data
     * @throws Exception if the child step execution fails
     */
    protected void executeChildStep(ChildStep childStep, WorkflowContext context, WorkflowData data) throws Exception {
        executeChildStepWithTiming(childStep, context, data);
    }

    /**
     * Executes a child step with timing metrics using the standard request/response/validate pattern.
     *
     * @param childStep the child step to execute
     * @param context the workflow execution context
     * @param data the shared workflow data
     * @throws Exception if the child step execution fails
     */
    protected void executeChildStepWithTiming(ChildStep childStep, WorkflowContext context, WorkflowData data) throws Exception {
        if (primitives == null) {
            throw new IllegalStateException("Primitives not injected into step: " + name);
        }

        Instant childStartTime = Instant.now();
        try {
            System.out.println("Processing Child: " + childStep.getName());

            // 1. Prepare request from context and data using request hook
            Object request = childStep.getRequestHook().apply(context, data);
            
            // 2. Execute response (calls Primitive) using response hook
            Object response = childStep.getResponseHook().apply(context, data);
            
            // 3. If response is not null, validate it using validate hook
            if (response != null && childStep.getValidateHook() != null) {
                childStep.getValidateHook().accept(response);
            }
            
            // Store results in workflow data
            if (response != null) {
                data.put(childStep.getName() + "Result", response);
            }
            data.put(childStep.getName() + "Completed", true);
            
            // Store child step metrics
            storeChildStepMetrics(childStep, context, data, childStartTime, Instant.now(), null);
        } catch (Exception e) {
            storeChildStepMetrics(childStep, context, data, childStartTime, Instant.now(), e.getMessage());
            throw e;
        }
    }

    /**
     * Stores child step execution metrics in workflow data.
     *
     * @param childStep the child step
     * @param context the workflow execution context
     * @param data the shared workflow data
     * @param startTime the start time
     * @param endTime the end time
     * @param errorMessage the error message if any
     */
    protected void storeChildStepMetrics(ChildStep childStep, WorkflowContext context, WorkflowData data,
                                        Instant startTime, Instant endTime, String errorMessage) {
        String childStepKey = "childStep_" + childStep.getName() + "_metrics";
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("childStepName", childStep.getName());
        metrics.put("parentStepName", getName());
        metrics.put("startTime", startTime);
        metrics.put("endTime", endTime);
        if (startTime != null && endTime != null) {
            metrics.put("durationMillis", java.time.Duration.between(startTime, endTime).toMillis());
        }
        metrics.put("errorMessage", errorMessage);
        data.put(childStepKey, metrics);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return parallel == step.parallel && 
               Objects.equals(name, step.name) && 
               Objects.equals(childSteps, step.childSteps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, childSteps, parallel);
    }

    @Override
    public String toString() {
        return "Step{" +
                "name='" + name + '\'' +
                ", childSteps=" + childSteps +
                ", parallel=" + parallel +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
