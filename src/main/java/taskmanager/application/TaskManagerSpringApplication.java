package taskmanager.application;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = { "taskmanager.repository" })
@EnableTransactionManagement
@SpringBootApplication
@ImportResource("classpath:taskmanager-beans-context.xml")
public class TaskManagerSpringApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TaskManagerSpringApplication.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
		app.run(args);

	}

}
