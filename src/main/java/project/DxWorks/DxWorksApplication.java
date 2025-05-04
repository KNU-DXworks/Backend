package project.DxWorks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DxWorksApplication {

	public static void main(String[] args) {
		SpringApplication.run(DxWorksApplication.class, args);
	}

}
