package com.uwf.workflow.api.cli;

import com.uwf.workflow.engine.WorkflowExecutor;
import com.uwf.workflow.primitive.api.Primitives;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Command line runner for workflow API.
 * Currently empty - CLI functionality will be implemented on client side later.
 */
@Component
public class WorkflowCliRunner implements CommandLineRunner {

    private final WorkflowExecutor workflowExecutor;
    private final Primitives primitives;

    @Autowired
    public WorkflowCliRunner(WorkflowExecutor workflowExecutor, Primitives primitives) {
        this.workflowExecutor = workflowExecutor;
        this.primitives = primitives;
    }

    @Override
    public void run(String... args) throws Exception {
        // CLI functionality removed - will be implemented on client side later
        // This runner is kept for dependency injection purposes
    }
}
