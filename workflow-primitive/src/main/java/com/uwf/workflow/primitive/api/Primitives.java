package com.uwf.workflow.primitive.api;

import com.uwf.workflow.primitive.api.annotation.Primitive;
import com.uwf.workflow.primitive.api.annotation.PrimitiveMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Registry for primitive operations.
 * Provides a clean way to register and access primitive operations.
 * Supports both interface-based registration and annotation-based registration.
 */
public class Primitives {
    private final Map<Class<?>, Object> interfaceRegistry = new HashMap<>();
    private final Map<String, PrimitiveMethod> methodRegistry = new HashMap<>();
    private final Map<String, Map<String, PrimitiveMethod>> clientRegistry = new HashMap<>();
    
    /**
     * Represents a group of primitive methods for a specific client.
     * Provides a fluent API for accessing client-specific primitives.
     */
    public class ClientPrimitives {
        private final String clientName;
        
        private ClientPrimitives(String clientName) {
            this.clientName = clientName;
        }
        
        /**
         * Retrieves a primitive method by name for this client.
         */
        public PrimitiveMethod method(String name) {
            Map<String, PrimitiveMethod> clientMethods = clientRegistry.get(clientName);
            if (clientMethods == null) {
                throw new IllegalArgumentException("No primitives registered for client: " + clientName);
            }
            PrimitiveMethod method = clientMethods.get(name);
            if (method == null) {
                throw new IllegalArgumentException("No primitive method '" + name + "' registered for client: " + clientName);
            }
            return method;
        }
        
        /**
         * Invokes a primitive method by name for this client.
         */
        public CompletableFuture<Object> invoke(String name, Object... args) {
            return method(name).invoke(args);
        }
        
        /**
         * Checks if a primitive method exists for this client.
         */
        public boolean hasMethod(String name) {
            Map<String, PrimitiveMethod> clientMethods = clientRegistry.get(clientName);
            return clientMethods != null && clientMethods.containsKey(name);
        }
        
        /**
         * Gets all method names for this client.
         */
        public java.util.Set<String> getMethodNames() {
            Map<String, PrimitiveMethod> clientMethods = clientRegistry.get(clientName);
            return clientMethods != null ? clientMethods.keySet() : java.util.Collections.emptySet();
        }
        
        /**
         * Gets the client name.
         */
        public String getClientName() {
            return clientName;
        }
    }

    /**
     * Registers a primitive implementation for a specific interface type.
     *
     * @param type the primitive interface type
     * @param impl the primitive implementation
     * @param <T> the primitive type
     */
    public <T> void register(Class<T> type, T impl) {
        interfaceRegistry.put(type, impl);
    }

    /**
     * Registers all methods annotated with @Primitive from the given object.
     *
     * @param instance the object instance containing annotated methods
     */
    public void registerAnnotated(Object instance) {
        Class<?> clazz = instance.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            Primitive annotation = method.getAnnotation(Primitive.class);
            if (annotation != null) {
                PrimitiveMethod primitiveMethod = new PrimitiveMethod(instance, method, annotation);
                String name = primitiveMethod.getName();
                String client = primitiveMethod.getClient();
                
                // Register in global method registry
                methodRegistry.put(name, primitiveMethod);
                
                // Register in client registry
                clientRegistry.computeIfAbsent(client, k -> new HashMap<>())
                             .put(name, primitiveMethod);
            }
        }
    }

    /**
     * Registers a specific method as a primitive operation.
     *
     * @param name the name to register the method under
     * @param instance the object instance containing the method
     * @param methodName the name of the method to register
     * @param parameterTypes the parameter types of the method
     */
    public void registerMethod(String name, Object instance, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = instance.getClass().getDeclaredMethod(methodName, parameterTypes);
            Primitive annotation = method.getAnnotation(Primitive.class);
            PrimitiveMethod primitiveMethod = new PrimitiveMethod(instance, method, 
                annotation != null ? annotation : new Primitive() {
                    @Override
                    public String name() {
                        return name;
                    }
                    
                    @Override
                    public String client() {
                        return "default";
                    }
                    
                    @Override
                    public String description() {
                        return "";
                    }
                    
                    @Override
                    public String category() {
                        return "manual";
                    }
                    
                    @Override
                    public Class<? extends java.lang.annotation.Annotation> annotationType() {
                        return Primitive.class;
                    }
                });
            methodRegistry.put(name, primitiveMethod);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method not found: " + methodName, e);
        }
    }

    /**
     * Retrieves a primitive implementation by interface type.
     *
     * @param type the primitive interface type
     * @param <T> the primitive type
     * @return the primitive implementation
     * @throws IllegalArgumentException if no primitive is registered for the type
     */
    @SuppressWarnings("unchecked")
    public <T> T ops(Class<T> type) {
        T primitive = (T) interfaceRegistry.get(type);
        if (primitive == null) {
            throw new IllegalArgumentException("No primitive registered for type: " + type.getName());
        }
        return primitive;
    }

    /**
     * Retrieves a primitive method by name.
     *
     * @param name the name of the primitive method
     * @return the PrimitiveMethod instance
     * @throws IllegalArgumentException if no primitive method is registered with the given name
     */
    public PrimitiveMethod method(String name) {
        PrimitiveMethod method = methodRegistry.get(name);
        if (method == null) {
            throw new IllegalArgumentException("No primitive method registered with name: " + name);
        }
        return method;
    }

    /**
     * Invokes a primitive method by name with the given arguments.
     *
     * @param name the name of the primitive method
     * @param args the arguments to pass to the method
     * @return a CompletableFuture with the result
     */
    public CompletableFuture<Object> invoke(String name, Object... args) {
        PrimitiveMethod method = method(name);
        return method.invoke(args);
    }

    /**
     * Checks if a primitive is registered for the given interface type.
     *
     * @param type the primitive interface type
     * @return true if a primitive is registered, false otherwise
     */
    public boolean hasPrimitive(Class<?> type) {
        return interfaceRegistry.containsKey(type);
    }

    /**
     * Checks if a primitive method is registered with the given name.
     *
     * @param name the name of the primitive method
     * @return true if a primitive method is registered, false otherwise
     */
    public boolean hasMethod(String name) {
        return methodRegistry.containsKey(name);
    }

    /**
     * Clears all registered primitives.
     */
    public void clear() {
        interfaceRegistry.clear();
        methodRegistry.clear();
        clientRegistry.clear();
    }

    // Helper methods for clean syntax
    
    /**
     * Gets the primitive operations instance.
     */
    public PrimitiveOps primitive() {
        return ops(PrimitiveOps.class);
    }
    
    /**
     * Gets a ClientPrimitives instance for the specified client.
     * 
     * @param clientName the name of the client
     * @return a ClientPrimitives instance for accessing client-specific primitives
     */
    public ClientPrimitives client(String clientName) {
        return new ClientPrimitives(clientName);
    }
    
    /**
     * Gets all registered client names.
     * 
     * @return a set of all client names that have registered primitives
     */
    public java.util.Set<String> getClientNames() {
        return clientRegistry.keySet();
    }
    
    /**
     * Checks if a client has any registered primitives.
     * 
     * @param clientName the name of the client
     * @return true if the client has registered primitives, false otherwise
     */
    public boolean hasClient(String clientName) {
        return clientRegistry.containsKey(clientName) && !clientRegistry.get(clientName).isEmpty();
    }
}
