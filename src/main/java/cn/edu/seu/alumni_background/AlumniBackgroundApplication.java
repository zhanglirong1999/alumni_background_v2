package cn.edu.seu.alumni_background;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class AlumniBackgroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlumniBackgroundApplication.class, args);
	}
}
