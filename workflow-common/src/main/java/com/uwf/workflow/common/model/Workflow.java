package com.uwf.workflow.common.model;

import com.uwf.workflow.primitive.api.Primitives;
import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a complete workflow with a unique ID, description, and ordered steps.
 */
public class Workflow {
    private final String id;
    private final String description;
    private final List<Step> steps;

    /**
     * Constructs a new workflow with the specified description.
     *
     * @param description the description of the workflow
     */
    public Workflow(String description) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.steps = new ArrayList<>();
    }

    /**
     * Constructs a new workflow with the specified ID and description.
     *
     * @param id          the unique ID of the workflow
     * @param description the description of the workflow
     */
    public Workflow(String id, String description) {
        this.id = id;
        this.description = description;
        this.steps = new ArrayList<>();
    }

    /**
     * Constructs a new workflow with the specified description and steps.
     *
     * @param description the description of the workflow
     * @param steps      the list of steps in this workflow
     */
    public Workflow(String description, List<Step> steps) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.steps = new ArrayList<>(steps);
    }

    /**
     * Constructs a new workflow with the specified ID, description, and steps.
     *
     * @param id          the unique ID of the workflow
     * @param description the description of the workflow
     * @param steps      the list of steps in this workflow
     */
    public Workflow(String id, String description, List<Step> steps) {
        this.id = id;
        this.description = description;
        this.steps = new ArrayList<>(steps);
    }

    /**
     * Gets the unique ID of this workflow.
     *
     * @return the workflow ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the description of this workflow.
     *
     * @return the workflow description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets an unmodifiable view of the steps in this workflow.
     *
     * @return the list of steps
     */
    public List<Step> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    /**
     * Adds a step to this workflow.
     *
     * @param step the step to add
     * @return this workflow for method chaining
     */
    public Workflow addStep(Step step) {
        steps.add(step);
        return this;
    }

    /**
     * Adds multiple steps to this workflow.
     *
     * @param stepsToAdd the steps to add
     * @return this workflow for method chaining
     */
    public Workflow addSteps(List<Step> stepsToAdd) {
        steps.addAll(stepsToAdd);
        return this;
    }

    /**
     * Gets the number of steps in this workflow.
     *
     * @return the number of steps
     */
    public int getStepCount() {
        return steps.size();
    }

    /**
     * Gets a step by its index.
     *
     * @param index the index of the step
     * @return the step at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Step getStep(int index) {
        return steps.get(index);
    }

    /**
     * Gets the total number of child steps across all steps.
     *
     * @return the total number of child steps
     */
    public int getTotalChildStepCount() {
        return steps.stream()
                .mapToInt(Step::getChildStepCount)
                .sum();
    }

    /**
     * Gets a child step by its global index across all steps.
     *
     * @param globalChildStepIndex the global child step index
     * @return the child step at the specified global index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public ChildStep getChildStepByGlobalIndex(int globalChildStepIndex) {
        int currentIndex = 0;
        for (Step step : steps) {
            int stepChildStepCount = step.getChildStepCount();
            if (globalChildStepIndex < currentIndex + stepChildStepCount) {
                return step.getChildStep(globalChildStepIndex - currentIndex);
            }
            currentIndex += stepChildStepCount;
        }
        throw new IndexOutOfBoundsException("Global child step index out of range: " + globalChildStepIndex);
    }

    /**
     * Gets the step and child step indices for a given global child step index.
     *
     * @param globalChildStepIndex the global child step index
     * @return an array containing [stepIndex, childStepIndex]
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public int[] getStepAndChildStepIndices(int globalChildStepIndex) {
        int currentIndex = 0;
        for (int stepIndex = 0; stepIndex < steps.size(); stepIndex++) {
            Step step = steps.get(stepIndex);
            int stepChildStepCount = step.getChildStepCount();
            if (globalChildStepIndex < currentIndex + stepChildStepCount) {
                return new int[]{stepIndex, globalChildStepIndex - currentIndex};
            }
            currentIndex += stepChildStepCount;
        }
        throw new IndexOutOfBoundsException("Global child step index out of range: " + globalChildStepIndex);
    }
    
    /**
     * Gets the current step for a given global child step index.
     *
     * @param globalChildStepIndex the global child step index
     * @return the current step, or null if index is at the end
     */
    public Step getCurrentStep(int globalChildStepIndex) {
        if (globalChildStepIndex >= getTotalChildStepCount()) {
            return null;
        }
        
        int[] indices = getStepAndChildStepIndices(globalChildStepIndex);
        return steps.get(indices[0]);
    }
    
    /**
     * Checks if the current step (for the given global child step index) is parallel.
     *
     * @param globalChildStepIndex the global child step index
     * @return true if the current step is parallel, false otherwise
     */
    public boolean isCurrentStepParallel(int globalChildStepIndex) {
        Step step = getCurrentStep(globalChildStepIndex);
        return step != null && step.isParallel();
    }
    
    /**
     * Gets all child steps in the current step for a given global child step index.
     *
     * @param globalChildStepIndex the global child step index
     * @return list of child steps in the current step
     */
    public List<ChildStep> getCurrentStepChildSteps(int globalChildStepIndex) {
        Step step = getCurrentStep(globalChildStepIndex);
        if (step == null) {
            return Collections.emptyList();
        }
        return step.getChildSteps();
    }
    
    /**
     * Gets the starting global index for a given step.
     *
     * @param stepIndex the step index
     * @return the starting global child step index for the step
     */
    public int getStepStartIndex(int stepIndex) {
        int startIndex = 0;
        for (int i = 0; i < stepIndex; i++) {
            startIndex += steps.get(i).getChildStepCount();
        }
        return startIndex;
    }

    /**
     * Checks if this workflow contains any steps.
     *
     * @return true if the workflow has steps, false otherwise
     */
    public boolean hasSteps() {
        return !steps.isEmpty();
    }

    /**
     * Injects primitives into all steps in this workflow.
     *
     * @param primitives the primitives registry to inject
     */
    public void setPrimitives(Primitives primitives) {
        for (Step step : steps) {
            step.setPrimitives(primitives);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workflow workflow = (Workflow) o;
        return Objects.equals(id, workflow.id) && 
               Objects.equals(description, workflow.description) && 
               Objects.equals(steps, workflow.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, steps);
    }

    @Override
    public String toString() {
        return "Workflow{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", steps=" + steps +
                '}';
    }
}
