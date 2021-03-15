package eu.codecache.rmd.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.model.UserLevel;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository uRepo;

	// We need user levels to add users
	@Autowired
	private UserLevelRepository ulRepo;

	@Test
	public void createNewUser() {
		// This kinda also tests to see if we can add & find userLevels 
		ulRepo.save(new UserLevel("user", "USER"));
		UserDTO user = new UserDTO(ulRepo.findByValue("USER"), "username", "password", "password");
		uRepo.save(user);
		assertThat(user.getUserID()).isNotNull();
	}
}
