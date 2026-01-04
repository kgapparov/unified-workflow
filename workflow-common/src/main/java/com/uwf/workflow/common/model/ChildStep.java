package com.uwf.workflow.common.model;

import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Represents a granular execution unit (the "Leaf") that holds the bound_method
 * and the request/response/validate hooks for interacting with primitives.
 * This is a simple data-carrying object that holds the "Hooks" for the lifecycle.
 * Execution logic is handled by the parent Step class.
 */
public class ChildStep {
    private final String name;
    private final BiFunction<WorkflowContext, WorkflowData, Object> requestHook;   // Prepare
    private final BiFunction<WorkflowContext, WorkflowData, Object> responseHook;  // Execute (calls Primitive)
    private final Consumer<Object> validateHook;                                   // Validate

    /**
     * Constructs a new ChildStep with the specified hooks.
     *
     * @param name the name of the child step
     * @param requestHook function to prepare the request from context and data
     * @param responseHook function to execute (calls Primitive) and return response
     * @param validateHook consumer to validate the response
     */
    public ChildStep(String name,
                     BiFunction<WorkflowContext, WorkflowData, Object> requestHook,
                     BiFunction<WorkflowContext, WorkflowData, Object> responseHook,
                     Consumer<Object> validateHook) {
        this.name = name;
        this.requestHook = requestHook;
        this.responseHook = responseHook;
        this.validateHook = validateHook;
    }

    /**
     * Gets the name of this child step.
     *
     * @return the child step name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the request hook function.
     *
     * @return the request hook
     */
    public BiFunction<WorkflowContext, WorkflowData, Object> getRequestHook() {
        return requestHook;
    }

    /**
     * Gets the response hook function.
     *
     * @return the response hook
     */
    public BiFunction<WorkflowContext, WorkflowData, Object> getResponseHook() {
        return responseHook;
    }

    /**
     * Gets the validate hook consumer.
     *
     * @return the validate hook
     */
    public Consumer<Object> getValidateHook() {
        return validateHook;
    }

    @Override
    public String toString() {
        return "ChildStep{" +
                "name='" + name + '\'' +
                '}';
    }
}
