# Workflow API Curl Commands Reference

## Base URL
All endpoints are available at: `http://localhost:8081/api/workflows`

## 1. List All Registered Workflows
**Endpoint:** `GET /api/workflows/list`

Returns a list of all registered workflows with their IDs, descriptions, and step counts.

```bash
# Basic request
curl -X GET http://localhost:8081/api/workflows/list

# With pretty JSON output
curl -X GET http://localhost:8081/api/workflows/list | jq .

# Example response:
# [
#   {
#     "id": "simple-demo",
#     "description": "Simple Demo Workflow - Hello World",
#     "stepCount": "3"
#   },
#   {
#     "id": "demo-workflow",
#     "description": "Demo Workflow - ETL Pipeline",
#     "stepCount": "5"
#   }
# ]
```

## 2. Run a Workflow by ID
**Endpoint:** `POST /api/workflows/run/{workflowId}`

Runs a pre-registered workflow by its ID.

```bash
# Run the simple-demo workflow
curl -X POST http://localhost:8081/api/workflows/run/simple-demo

# Run the demo-workflow
curl -X POST http://localhost:8081/api/workflows/run/demo-workflow

# With verbose output
curl -v -X POST http://localhost:8081/api/workflows/run/simple-demo

# Example response:
# {
#   "runId": "550e8400-e29b-41d4-a716-446655440000",
#   "message": "Workflow submitted for execution",
#   "workflowId": "simple-demo"
# }
```

## 3. Run a Custom Workflow
**Endpoint:** `POST /api/workflows/run`

Runs a custom workflow definition provided in the request body.

```bash
# Run a custom workflow
curl -X POST http://localhost:8081/api/workflows/run \
  -H "Content-Type: application/json" \
  -d '{
    "id": "custom-workflow-1",
    "description": "My Custom Workflow",
    "steps": [
      {
        "name": "Step 1",
        "description": "First step"
      },
      {
        "name": "Step 2", 
        "description": "Second step"
      }
    ]
  }'

# Example response:
# {
#   "runId": "550e8400-e29b-41d4-a716-446655440001",
#   "message": "Workflow submitted for execution",
#   "workflowId": "custom-workflow-1"
# }
```

## 4. Get Workflow Status
**Endpoint:** `GET /api/workflows/status/{runId}`

Gets the status of a workflow execution by its run ID.

```bash
# Get status for a specific run
curl -X GET http://localhost:8081/api/workflows/status/550e8400-e29b-41d4-a716-446655440000

# With pretty JSON output
curl -X GET http://localhost:8081/api/workflows/status/550e8400-e29b-41d4-a716-446655440000 | jq .

# Example response:
# {
#   "runId": "550e8400-e29b-41d4-a716-446655440000",
#   "workflowId": "simple-demo",
#   "status": "COMPLETED",
#   "startTime": "2026-01-03T23:09:57.012Z",
#   "endTime": "2026-01-03T23:09:57.015Z",
#   "currentStep": null,
#   "error": null
# }
```

## 5. Get Workflow Data
**Endpoint:** `GET /api/workflows/data/{runId}`

Gets the data/output from a workflow execution by its run ID.

```bash
# Get data for a specific run
curl -X GET http://localhost:8081/api/workflows/data/550e8400-e29b-41d4-a716-446655440000

# With pretty JSON output
curl -X GET http://localhost:8081/api/workflows/data/550e8400-e29b-41d4-a716-446655440000 | jq .

# Example response:
# {
#   "extractedData": "Sample data extracted from source",
#   "extractionTimestamp": 1735957797012,
#   "transformedData": "SAMPLE DATA EXTRACTED FROM SOURCE [TRANSFORMED]",
#   "loadedData": "SAMPLE DATA EXTRACTED FROM SOURCE [TRANSFORMED] -> Loaded to destination",
#   "loadComplete": true
# }
```

## Complete Workflow Example

Here's a complete example showing the typical workflow lifecycle:

```bash
# 1. List available workflows
curl -X GET http://localhost:8081/api/workflows/list | jq .

# 2. Run a workflow (save the runId from the response)
RUN_ID=$(curl -s -X POST http://localhost:8081/api/workflows/run/simple-demo | jq -r '.runId')
echo "Run ID: $RUN_ID"

# 3. Check workflow status
curl -X GET http://localhost:8081/api/workflows/status/$RUN_ID | jq .

# 4. Get workflow data (after completion)
curl -X GET http://localhost:8081/api/workflows/data/$RUN_ID | jq .
```

## Using Different Ports

If running on a different port (e.g., 8085), update the base URL:

```bash
# Using port 8085
curl -X GET http://localhost:8085/api/workflows/list

# Using environment variable
API_URL="http://localhost:8085"
curl -X GET $API_URL/api/workflows/list
```

## Troubleshooting

### Check if server is running:
```bash
curl -I http://localhost:8081/api/workflows/list
```

### Get detailed error information:
```bash
curl -v -X POST http://localhost:8081/api/workflows/run/nonexistent-workflow
```

### Format JSON responses:
```bash
# Install jq if not available: brew install jq (macOS) or apt-get install jq (Linux)
curl -s http://localhost:8081/api/workflows/list | jq .
```

## Notes
- The server runs on port 8081 by default
- Workflows execute asynchronously
- Use the `runId` returned from the run endpoints to track execution status and data
- Pre-registered workflows: `simple-demo` and `demo-workflow`
