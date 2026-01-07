# Running WorkflowClientExample

## Prerequisites

1. **Java 11+** installed
2. **Gradle** installed (or use the provided gradlew wrapper)
3. **Workflow API Server** running (optional, for full functionality)

## Option 1: Run with Simple Client (No API Server Required)

The `WorkflowClientExample` can run with the simple client implementation that shows the API usage patterns without requiring an actual API server.

```bash
# From the project root directory
./gradlew :workflow-service-client:run
```

This will execute the example which demonstrates:
- Creating workflow clients with different configurations
- Submitting workflows
- Getting workflow status
- Listing workflows
- Getting workflow data
- Canceling workflows
- Getting workflow definitions

**Note**: The simple client shows "Simple client - not implemented" messages because it's a stub implementation for demonstration purposes.

## Option 2: Run with Real API Server

### Step 1: Start the Workflow API Server

First, you need to start the workflow API server:

```bash
# From the project root directory
./gradlew :workflow-api:bootRun
```

The API server will start on `http://localhost:8080` by default.

### Step 2: Run the Client Example

In a separate terminal, run the client example:

```bash
# From the project root directory
./gradlew :workflow-service-client:run
```

### Step 3: Verify the Connection

The example will connect to the running API server and demonstrate real API calls.

## Option 3: Run Spring Boot Application

You can also run the full Spring Boot application which includes the `RestWorkflowClientService`:

```bash
# From the project root directory
./gradlew :workflow-service-client:bootRun
```

This starts the workflow service client application on port 8082 (configurable in `application.yml`).

## Option 4: Create Your Own Example

You can create your own Java class with a main method:

```java
package com.uwf.workflow.client.example;

import com.uwf.workflow.client.WorkflowClient;
import com.uwf.workflow.client.WorkflowClientBuilder;
import com.uwf.workflow.common.model.Workflow;

public class MyExample {
    public static void main(String[] args) {
        // Create client with custom configuration
        WorkflowClient client = new WorkflowClient(
            WorkflowClientBuilder.create()
                .baseUrl("http://localhost:8080")
                .build()
        );
        
        // Create and submit a workflow
        Workflow workflow = new Workflow("My Workflow");
        client.submitWorkflow(workflow)
            .doOnNext(output -> {
                if (output.isSuccess()) {
                    System.out.println("Success! Run ID: " + output.getRunId());
                } else {
                    System.out.println("Failed: " + output.getErrorMessage());
                }
            })
            .subscribe();
    }
}
```

Compile and run with:
```bash
./gradlew :workflow-service-client:compileJava
java -cp "build/classes/java/main:build/resources/main:$(find ~/.gradle/caches -name '*.jar' | tr '\n' ':')" com.uwf.workflow.client.example.MyExample
```

## Configuration

The client can be configured via `application.yml`:

```yaml
workflow:
  client:
    base-url: http://localhost:8080
    timeout: 5000
    retry:
      max-attempts: 3
      backoff-delay: 1000
    server-port: 8082
```

Or programmatically via `WorkflowClientBuilder`:

```java
WorkflowClient client = new WorkflowClient(
    WorkflowClientBuilder.create()
        .baseUrl("http://custom-host:9090")
        .clientType(WorkflowClientBuilder.ClientType.REST)
        .build()
);
```

## Troubleshooting

### 1. "Connection refused" errors
- Ensure the workflow API server is running on the correct port (default: 8080)
- Check the base URL configuration

### 2. "Simple client - not implemented" messages
- This is expected when using the simple client implementation
- Switch to `RestWorkflowClientService` for real API calls

### 3. Compilation errors
- Ensure all dependencies are built: `./gradlew build`
- Check that the `workflow-primitive` module is available

### 4. Reactor not processing events
- The example uses reactive programming; ensure you're subscribing to the reactive streams
- Add a small delay at the end of your main method to allow async operations to complete:
  ```java
  Thread.sleep(2000); // Wait for async operations
  ```

## Testing

Run the unit tests to verify the client implementation:

```bash
./gradlew :workflow-service-client:test
```

## API Endpoints

The client communicates with these API endpoints (configurable in `WorkflowClientConfig`):

- `POST /api/workflows/run` - Submit a new workflow
- `POST /api/workflows/run/{workflowId}` - Submit workflow by ID
- `GET /api/workflows/status/{runId}` - Get workflow status
- `GET /api/workflows/data/{runId}` - Get workflow data
- `GET /api/workflows/list` - List all workflows
- `POST /api/workflows/cancel/{runId}` - Cancel a workflow
- `GET /api/workflows/definition/{workflowId}` - Get workflow definition
