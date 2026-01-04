package com.uwf.workflow.api.config;

import com.uwf.workflow.primitive.api.Primitives;
import com.uwf.workflow.primitive.api.PrimitiveOps;
import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

@Configuration
public class PrimitivesConfig {

    @Bean
    public Primitives primitives() {
        Primitives primitives = new Primitives();
        
        // Register a simple PrimitiveOps implementation
        PrimitiveOps primitiveOps = new PrimitiveOps() {
            @Override
            public CompletableFuture<Void> logInfo(String message) {
                System.out.println("[INFO] " + message);
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> logWarning(String message) {
                System.out.println("[WARN] " + message);
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> logError(String message) {
                System.out.println("[ERROR] " + message);
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> logDebug(String message) {
                System.out.println("[DEBUG] " + message);
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Long> getCurrentTimestamp() {
                return CompletableFuture.completedFuture(System.currentTimeMillis());
            }

            @Override
            public CompletableFuture<Void> sleep(long millis) {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<String> generateRandomId() {
                return CompletableFuture.completedFuture(java.util.UUID.randomUUID().toString());
            }

            @Override
            public CompletableFuture<Void> validate(boolean condition, String message) {
                if (!condition) {
                    throw new IllegalArgumentException("Validation failed: " + message);
                }
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public CompletableFuture<Void> storeData(WorkflowContext context, String key, Object value) {
                // For demo purposes, we'll just print
                System.out.println("[Store Data] " + key + " = " + value);
                return CompletableFuture.completedFuture(null);
            }

            @Override
            public <T> CompletableFuture<T> retrieveData(WorkflowContext context, String key, Class<T> type) {
                // For demo purposes, return null
                System.out.println("[Retrieve Data] " + key + " (type: " + type.getSimpleName() + ")");
                return CompletableFuture.completedFuture(null);
            }
        };
        
        primitives.register(PrimitiveOps.class, primitiveOps);
        return primitives;
    }
}
