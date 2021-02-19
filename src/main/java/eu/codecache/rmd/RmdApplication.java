package eu.codecache.rmd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import eu.codecache.rmd.repositories.CommentRepository;

@SpringBootApplication
public class RmdApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmdApplication.class, args);
	}

	@Bean
	public CommandLineRunner h2Filler(CommentRepository comments) {
		return (args) -> {
			System.out.println("CommandLineRunner ajetaan");
		};
	}

}
