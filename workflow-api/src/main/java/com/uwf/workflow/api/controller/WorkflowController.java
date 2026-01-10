package com.uwf.workflow.api.controller;

import com.uwf.workflow.common.model.Workflow;
import com.uwf.workflow.engine.WorkflowExecutor;
import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;
import com.uwf.workflow.engine.state.StateManagement;
import com.uwf.workflow.registry.WorkflowRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowExecutor workflowExecutor;
    private final StateManagement stateManagement;
    private final WorkflowRegistry workflowRegistry;

    @Autowired
    public WorkflowController(WorkflowExecutor workflowExecutor, 
                              StateManagement stateManagement,
                              WorkflowRegistry workflowRegistry) {
        this.workflowExecutor = workflowExecutor;
        this.stateManagement = stateManagement;
        this.workflowRegistry = workflowRegistry;
    }

    @PostMapping("/run")
    public ResponseEntity<Map<String, String>> runWorkflow(@RequestBody Workflow workflow) {
        String runId = workflowExecutor.submitWorkflow(workflow);
        return ResponseEntity.ok(Map.of(
            "runId", runId,
            "message", "Workflow submitted for execution",
            "workflowId", workflow.getId()
        ));
    }

    @PostMapping("/run/{workflowId}")
    public ResponseEntity<Map<String, String>> runWorkflowById(@PathVariable String workflowId) {
        String runId = workflowExecutor.submitWorkflow(workflowId);
        return ResponseEntity.ok(Map.of(
            "runId", runId,
            "message", "Workflow submitted for execution",
            "workflowId", workflowId
        ));
    }

    @GetMapping("/status/{runId}")
    public ResponseEntity<WorkflowContext> getWorkflowStatus(@PathVariable String runId) {
        WorkflowContext context = stateManagement.getContext(runId);
        if (context == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(context);
    }

    @GetMapping("/data/{runId}")
    public ResponseEntity<Map<String, Object>> getWorkflowData(@PathVariable String runId) {
        WorkflowData workflowData = stateManagement.getData(runId);
        if (workflowData == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> data = workflowData.toMap();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/metrics/{runId}")
    public ResponseEntity<Map<String, Object>> getWorkflowMetrics(@PathVariable String runId) {
        WorkflowData workflowData = stateManagement.getData(runId);
        WorkflowContext context = stateManagement.getContext(runId);
        
        if (workflowData == null || context == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> metrics = new HashMap<>();
        
        // Add workflow context
        metrics.put("workflowContext", context);
        
        // Add workflow metrics
        Map<String, Object> workflowMetrics = workflowData.get("workflow_metrics", Map.class);
        if (workflowMetrics != null) {
            metrics.put("workflowMetrics", workflowMetrics);
        }
        
        // Collect all step metrics
        Map<String, Object> stepMetrics = new HashMap<>();
        Map<String, Object> childStepMetrics = new HashMap<>();
        
        Map<String, Object> dataMap = workflowData.toMap();
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("step_") && key.endsWith("_metrics")) {
                stepMetrics.put(key, entry.getValue());
            } else if (key.startsWith("childStep_") && key.endsWith("_metrics")) {
                childStepMetrics.put(key, entry.getValue());
            }
        }
        
        metrics.put("stepMetrics", stepMetrics);
        metrics.put("childStepMetrics", childStepMetrics);
        
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, String>>> listWorkflows() {
        String[] workflowIds = workflowRegistry.getAllWorkflowIds();
        List<Map<String, String>> workflows = new ArrayList<>();
        
        for (String workflowId : workflowIds) {
            workflowRegistry.getWorkflow(workflowId).ifPresent(workflow -> {
                Map<String, String> workflowInfo = new HashMap<>();
                workflowInfo.put("id", workflowId);
                workflowInfo.put("description", workflow.getDescription());
                workflowInfo.put("stepCount", String.valueOf(workflow.getStepCount()));
                workflows.add(workflowInfo);
            });
        }
        
        return ResponseEntity.ok(workflows);
    }
}
