package com.uwf.workflow.primitive.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method as a primitive operation.
 * Methods annotated with @Primitive can be automatically discovered
 * and registered in the Primitives registry.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Primitive {
    /**
     * The name under which this primitive operation should be registered.
     * If not specified, the method name will be used.
     */
    String name() default "";
    
    /**
     * The client/group name for this primitive operation.
     * Primitives can be grouped by client for easier organization.
     */
    String client() default "default";
    
    /**
     * A description of what this primitive operation does.
     */
    String description() default "";
    
    /**
     * The category of this primitive operation (e.g., "logging", "validation", "utility").
     */
    String category() default "general";
}
