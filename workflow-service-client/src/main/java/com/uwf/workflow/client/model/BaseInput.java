package com.uwf.workflow.client.model;

import java.io.Serializable;

/**
 * Base class for all service input parameters.
 * Provides common functionality for input validation and serialization.
 */
public abstract class BaseInput implements Serializable {
    
    /**
     * Validates the input parameters.
     * 
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        // Base implementation does nothing
        // Subclasses should override to provide validation
    }
    
    /**
     * Returns a string representation of the input for logging/debugging.
     * 
     * @return string representation
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
