package com.uwf.workflow.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
    + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
    + "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration"
})
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
class WorkflowServiceClientApplicationTests {

    @Test
    void contextLoads() {
        // Test that the Spring context loads successfully
    }
}
