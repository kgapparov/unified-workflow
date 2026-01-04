package com.uwf.workflow.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = {
    "com.uwf.workflow.api",
    "com.uwf.workflow.engine",
    "com.uwf.workflow.registry",
    "com.uwf.workflow.queue",
    "com.uwf.workflow.primitive",
    "com.uwf.workflow.common"
})
public class WorkflowApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApiApplication.class, args);
    }
}
