package com.uwf.workflow.api.config;

import com.uwf.workflow.common.model.ChildStep;
import com.uwf.workflow.common.model.Step;
import com.uwf.workflow.common.model.Workflow;
import com.uwf.workflow.primitive.api.Primitives;
import com.uwf.workflow.primitive.model.WorkflowContext;
import com.uwf.workflow.primitive.model.WorkflowData;
import com.uwf.workflow.registry.WorkflowRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoWorkflowConfig {

    private final Primitives primitives;
    private final WorkflowRegistry workflowRegistry;

    @Autowired
    public DemoWorkflowConfig(Primitives primitives, WorkflowRegistry workflowRegistry) {
        this.primitives = primitives;
        this.workflowRegistry = workflowRegistry;
    }

    @Bean
    public Workflow demoWorkflow() {
        // Create a comprehensive demo workflow
        Workflow workflow = new Workflow("demo-workflow", "Demo Workflow - ETL Pipeline");
        
        // Step 1: Extract data from source
        Step extractStep = new Step("Extract") {
            @Override
            public void run(WorkflowContext context, WorkflowData data) throws Exception {
                executeWithTiming(context, data, () -> {
                    System.out.println("[Demo Workflow] Extracting data from source...");
                    // Simulate data extraction
                    data.put("source", "database");
                    data.put("recordsExtracted", 100);
                    data.put("extractionTime", System.currentTimeMillis());
                    // Simulate some processing time
                    Thread.sleep(500);
                });
            }
        };
        
        // Step 2: Transform data
        Step transformStep = new Step("Transform") {
            @Override
            public void run(WorkflowContext context, WorkflowData data) throws Exception {
                executeWithTiming(context, data, () -> {
                    System.out.println("[Demo Workflow] Transforming data...");
                    Integer records = (Integer) data.get("recordsExtracted");
                    if (records != null) {
                        // Simulate data transformation
                        data.put("recordsTransformed", records);
                        data.put("transformationType", "normalization");
                        data.put("qualityScore", 95);
                    }
                    // Simulate some processing time
                    Thread.sleep(300);
                });
            }
        };
        
        // Step 3: Validate data
        Step validateStep = new Step("Validate") {
            @Override
            public void run(WorkflowContext context, WorkflowData data) throws Exception {
                executeWithTiming(context, data, () -> {
                    System.out.println("[Demo Workflow] Validating data...");
                    Integer qualityScore = (Integer) data.get("qualityScore");
                    if (qualityScore != null && qualityScore > 90) {
                        data.put("validationPassed", true);
                        data.put("validationMessage", "Data quality meets standards");
                    } else {
                        data.put("validationPassed", false);
                        data.put("validationMessage", "Data quality below threshold");
                    }
                    // Simulate some processing time
                    Thread.sleep(200);
                });
            }
        };
        
        // Step 4: Load data to destination
        Step loadStep = new Step("Load") {
            @Override
            public void run(WorkflowContext context, WorkflowData data) throws Exception {
                executeWithTiming(context, data, () -> {
                    System.out.println("[Demo Workflow] Loading data to destination...");
                    Boolean validationPassed = (Boolean) data.get("validationPassed");
                    if (validationPassed != null && validationPassed) {
                        data.put("destination", "data-warehouse");
                        data.put("loadSuccessful", true);
                        data.put("recordsLoaded", data.get("recordsTransformed"));
                        data.put("completionTime", System.currentTimeMillis());
                    } else {
                        data.put("loadSuccessful", false);
                        data.put("error", "Validation failed, skipping load");
                    }
                    // Simulate some processing time
                    Thread.sleep(400);
                });
            }
        };
        
        // Step 5: Generate report
        Step reportStep = new Step("Generate Report") {
            @Override
            public void run(WorkflowContext context, WorkflowData data) throws Exception {
                executeWithTiming(context, data, () -> {
                    System.out.println("[Demo Workflow] Generating report...");
                    Boolean loadSuccessful = (Boolean) data.get("loadSuccessful");
                    if (loadSuccessful != null && loadSuccessful) {
                        data.put("reportGenerated", true);
                        data.put("reportId", "REPORT-" + System.currentTimeMillis());
                        data.put("summary", "ETL process completed successfully");
                    } else {
                        data.put("reportGenerated", false);
                        data.put("summary", "ETL process failed");
                    }
                    // Simulate some processing time
                    Thread.sleep(250);
                });
            }
        };
        
        // Add all steps to workflow
        workflow.addStep(extractStep)
                .addStep(transformStep)
                .addStep(validateStep)
                .addStep(loadStep)
                .addStep(reportStep);
        
        // Inject primitives
        workflow.setPrimitives(primitives);
        
        // Register the workflow
        workflowRegistry.registerWorkflow(workflow);
        System.out.println("[Demo Workflow] Demo workflow registered with ID: " + workflow.getId());
        
        return workflow;
    }
    
    @Bean
    public Workflow simpleDemoWorkflow() {
        // Create a simpler demo workflow for quick testing
        Workflow workflow = new Workflow("simple-demo", "Simple Demo Workflow - Hello World");
        
        Step helloStep = new Step("Hello") {
            @Override
            public void run(WorkflowContext context, WorkflowData data) throws Exception {
                executeWithTiming(context, data, () -> {
                    System.out.println("[Simple Demo] Hello from workflow!");
                    data.put("message", "Hello, World!");
                    data.put("timestamp", System.currentTimeMillis());
                });
            }
        };
        
        Step processStep = new Step("Process") {
            @Override
            public void run(WorkflowContext context, WorkflowData data) throws Exception {
                executeWithTiming(context, data, () -> {
                    System.out.println("[Simple Demo] Processing data...");
                    String message = (String) data.get("message");
                    if (message != null) {
                        data.put("processedMessage", message.toUpperCase() + " - PROCESSED");
                    }
                    Thread.sleep(100);
                });
            }
        };
        
        Step completeStep = new Step("Complete") {
            @Override
            public void run(WorkflowContext context, WorkflowData data) throws Exception {
                executeWithTiming(context, data, () -> {
                    System.out.println("[Simple Demo] Completing workflow...");
                    data.put("completed", true);
                    data.put("finalMessage", "Workflow execution completed successfully");
                });
            }
        };
        
        workflow.addStep(helloStep)
                .addStep(processStep)
                .addStep(completeStep);
        
        workflow.setPrimitives(primitives);
        workflowRegistry.registerWorkflow(workflow);
        System.out.println("[Demo Workflow] Simple demo workflow registered with ID: " + workflow.getId());
        
        return workflow;
    }
}
