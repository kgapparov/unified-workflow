package com.uwf.workflow.primitive.api.annotation;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a method annotated with @Primitive that can be invoked as a primitive operation.
 */
public class PrimitiveMethod {
    private final Object target;
    private final Method method;
    private final Primitive annotation;
    private final String name;

    public PrimitiveMethod(Object target, Method method, Primitive annotation) {
        this.target = target;
        this.method = method;
        this.annotation = annotation;
        this.name = annotation.name().isEmpty() ? method.getName() : annotation.name();
        
        // Make the method accessible if it's not public
        if (!method.canAccess(target)) {
            method.setAccessible(true);
        }
    }

    /**
     * Gets the name of this primitive operation.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description from the annotation.
     */
    public String getDescription() {
        return annotation.description();
    }

    /**
     * Gets the client/group name from the annotation.
     */
    public String getClient() {
        return annotation.client();
    }

    /**
     * Gets the category from the annotation.
     */
    public String getCategory() {
        return annotation.category();
    }

    /**
     * Gets the underlying method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Invokes the primitive method with the given arguments.
     * Wraps the result in a CompletableFuture if it's not already one.
     */
    @SuppressWarnings("unchecked")
    public CompletableFuture<Object> invoke(Object... args) {
        try {
            Object result = method.invoke(target, args);
            if (result instanceof CompletableFuture) {
                return (CompletableFuture<Object>) result;
            } else {
                return CompletableFuture.completedFuture(result);
            }
        } catch (Exception e) {
            CompletableFuture<Object> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    /**
     * Gets the parameter types of the method.
     */
    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    /**
     * Gets the return type of the method.
     */
    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    @Override
    public String toString() {
        return "PrimitiveMethod{" +
                "name='" + name + '\'' +
                ", method=" + method.getName() +
                ", category='" + getCategory() + '\'' +
                '}';
    }
}
