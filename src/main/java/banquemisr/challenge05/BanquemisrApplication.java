package banquemisr.challenge05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BanquemisrApplication {

	public static void main(String[] args) {
		SpringApplication.run(BanquemisrApplication.class, args);
	}

}
