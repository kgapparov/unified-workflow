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
