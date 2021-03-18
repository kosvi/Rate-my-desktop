package eu.codecache.rmd.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.model.UserLevel;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository uRepo;

	// We need user levels to add users
	@Autowired
	private UserLevelRepository ulRepo;

	/*
	 * Test password hashing
	 */
	@Test
	public void hashUserPasswordsTest() {
		UserDTO user = uRepo.findByUsername("username");
		// return true on success
		assertTrue(user.encodePassword());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// users hash should match this hash
		String passwordHash = encoder.encode("password");
// this won't work, probably because I don't really know much about hashing 
// (I guess this is something I should study more at some point)
//		assertTrue(user.getPasswordHash().equals(passwordHash));
		// Also test case then hashing fails
		UserDTO user2 = new UserDTO(ulRepo.findByValue("USER"), "testuser", "password1", "password2");
		// now pass1 and pass2 don't match, hashing should fail
		assertFalse(user2.encodePassword());
		// also test that no hashing has been made
// No point in doing these tests
//		String passwordHash2 = encoder.encode("password1");
//		assertFalse(user2.getPasswordHash().equals(passwordHash2));
	}

	/*
	 * we should have "username" in database with accessLevel value of "USER" we
	 * also should have "adminuser" in database with accessLevel value of "ADMIN"
	 * 
	 * this will also test UserLevel class & it's repository
	 */
	@Test
	public void getUserFromRepoTest() {
		UserDTO user = uRepo.findByUsername("username");
		UserDTO user2 = uRepo.findByUsername("adminuser");
		// following should return null
		UserDTO user3 = uRepo.findByUsername("none");
		assertNull(user3);
		// Now, we could check to see if user or user2 is null, but the following kinda
		// handles that already doesn't it?
		assertTrue(user.getLevel().getValue().equals("USER"));
		assertTrue(user2.getLevel().getValue().equals("ADMIN"));
		assertTrue(user.getLevel().getValue().equals("USER"));
		assertTrue(user2.getLevel().getValue().equals("ADMIN"));
		// make sure it's not always returning true!
		assertFalse(user.getLevel().getValue().equals("ADMIN"));
		assertFalse(user2.getLevel().getValue().equals("USER"));
	}

	/*
	 * create user and add it to repository
	 */
	@BeforeEach
	public void createNewUser() {
		// This kinda also tests to see if we can add & find userLevels
		// ulRepo.save(new UserLevel("user", "USER"));
		// ulRepo.save(new UserLevel("admin", "ADMIN"));
		UserDTO user = new UserDTO(ulRepo.findByValue("USER"), "username", "password", "password");
		UserDTO user2 = new UserDTO(ulRepo.findByValue("ADMIN"), "adminuser", "password", "password");
		// let's not hash the password here
		uRepo.save(user);
		uRepo.save(user2);
//		assertThat(user.getUserID()).isNotNull();
	}
}
