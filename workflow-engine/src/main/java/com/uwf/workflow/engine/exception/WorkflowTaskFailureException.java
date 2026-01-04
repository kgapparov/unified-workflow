package com.uwf.workflow.engine.exception;

/**
 * Abstract base class for all workflow task failure exceptions.
 * Extends RuntimeException to allow unchecked exception handling.
 */
public abstract class WorkflowTaskFailureException extends RuntimeException {

    /**
     * Constructs a new workflow task failure exception with the specified detail message.
     *
     * @param message the detail message
     */
    public WorkflowTaskFailureException(String message) {
        super(message);
    }

    /**
     * Constructs a new workflow task failure exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public WorkflowTaskFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new workflow task failure exception with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public WorkflowTaskFailureException(Throwable cause) {
        super(cause);
    }
}
