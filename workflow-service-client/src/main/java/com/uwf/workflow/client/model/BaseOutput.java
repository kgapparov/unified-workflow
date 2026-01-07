package com.uwf.workflow.client.model;

import java.io.Serializable;

/**
 * Base class for all service output/results.
 * Provides common functionality for result handling and serialization.
 */
public abstract class BaseOutput implements Serializable {
    
    private boolean success;
    private String errorMessage;
    
    /**
     * Creates a successful output.
     */
    protected BaseOutput() {
        this.success = true;
        this.errorMessage = null;
    }
    
    /**
     * Creates an output with specified success status.
     * 
     * @param success whether the operation was successful
     * @param errorMessage error message if operation failed
     */
    protected BaseOutput(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    /**
     * Returns whether the operation was successful.
     * 
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * Returns the error message if the operation failed.
     * 
     * @return error message or null if successful
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Creates a successful output instance.
     * 
     * @param <T> the output type
     * @param output the output instance
     * @return the output instance marked as successful
     */
    public static <T extends BaseOutput> T success(T output) {
        // Output is already marked as successful by default constructor
        return output;
    }
    
    /**
     * Creates a failed output instance.
     * 
     * @param <T> the output type
     * @param output the output instance
     * @param errorMessage the error message
     * @return the output instance marked as failed
     */
    public static <T extends BaseOutput> T failure(T output, String errorMessage) {
        // This would require reflection to set fields, so we'll use a different approach
        // Subclasses should provide their own failure factory methods
        throw new UnsupportedOperationException("Use subclass-specific failure factory method");
    }
    
    /**
     * Returns a string representation of the output for logging/debugging.
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [success=" + success + 
               (errorMessage != null ? ", errorMessage=" + errorMessage : "") + "]";
    }
}
