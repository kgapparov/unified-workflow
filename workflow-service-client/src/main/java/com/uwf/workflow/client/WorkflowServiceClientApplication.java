package com.uwf.workflow.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Workflow Service Client.
 * This client provides a way to interact with workflow services
 * through REST APIs and reactive programming.
 */
@SpringBootApplication
public class WorkflowServiceClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowServiceClientApplication.class, args);
    }
}
