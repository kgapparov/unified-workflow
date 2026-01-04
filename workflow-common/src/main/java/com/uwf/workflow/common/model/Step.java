package com.uwf.workflow.common.model;

import com.uwf.workflow.primitive.api.Primitives;
import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
     * Executes a child step using the standard request/response/validate pattern.
     * This is a helper method that concrete Step implementations can use.
     *
     * @param childStep the child step to execute
     * @param context the workflow execution context
     * @param data the shared workflow data
     * @throws Exception if the child step execution fails
     */
    protected void executeChildStep(ChildStep childStep, WorkflowContext context, WorkflowData data) throws Exception {
        if (primitives == null) {
            throw new IllegalStateException("Primitives not injected into step: " + name);
        }

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
                '}';
    }
}
