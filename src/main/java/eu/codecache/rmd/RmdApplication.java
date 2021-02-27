package eu.codecache.rmd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.User;
import eu.codecache.rmd.model.UserLevel;
import eu.codecache.rmd.repositories.CommentRepository;
import eu.codecache.rmd.repositories.ScreenshotRepository;
import eu.codecache.rmd.repositories.UserLevelRepository;
import eu.codecache.rmd.repositories.UserRepository;

@SpringBootApplication
public class RmdApplication {

	public static void main(String[] args) {
		SpringApplication.run(RmdApplication.class, args);
	}

	@Bean
	public CommandLineRunner h2Filler(UserLevelRepository ulRepo, UserRepository uRepo, ScreenshotRepository ssRepo,
			CommentRepository cRepo) {
		return (args) -> {
			System.out.println("CommandLineRunner ajetaan");
			ulRepo.save(new UserLevel("Admin", "ADMIN"));
			ulRepo.save(new UserLevel("User", "USER"));
			uRepo.save(new User(ulRepo.findByValue("USER").get(0), "ville", "ville"));
			ssRepo.save(new Screenshot(uRepo.findByUsername("ville").get(0), "Testikuva", "123456789.png"));
			ssRepo.save(new Screenshot(uRepo.findByUsername("ville").get(0), "Vihreä teema", "foobar.jpg"));
		};
	}

}
