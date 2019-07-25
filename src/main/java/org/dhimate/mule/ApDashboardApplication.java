package org.dhimate.mule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(basePackages = {"org.dhimate.mule"})
public class ApDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApDashboardApplication.class, args);
	}

}
