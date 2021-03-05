package eu.codecache.rmd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;
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
			uRepo.save(new UserDTO(ulRepo.findByValue("USER"), "user",
					"$2a$10$wHhyIY3iLDPLO7Z7kRZI4OQaX6fPAT3teB6iMI0k3gqTgWG1o32uK"));
			UserDTO user2 = new UserDTO(ulRepo.findByValue("ADMIN"), "yllapito", "yllapito1", "yllapito1");
			user2.encodePassword();
			uRepo.save(user2);
			ssRepo.save(new Screenshot(uRepo.findByUsername("user"), "Testikuva", "123456789.png"));
			ssRepo.save(new Screenshot(uRepo.findByUsername("user"), "Vihreä teema", "foobar.jpg"));
		};
	}

}
