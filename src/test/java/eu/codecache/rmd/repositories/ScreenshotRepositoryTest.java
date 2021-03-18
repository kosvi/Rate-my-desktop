package eu.codecache.rmd.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import eu.codecache.rmd.model.Rating;
import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.model.UserLevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ScreenshotRepositoryTest {

	@Autowired
	private ScreenshotRepository ssRepo;

	@Autowired
	private UserRepository uRepo;

	@Autowired
	private UserLevelRepository ulRepo;

	/*
	 * Test finding a screenshot from database with id This also test finding by
	 * name
	 */
	@Test
	public void findScreenshotById() {
		Screenshot ss = ssRepo.findByScreenshotName("Screenshot100");
		assertThat(ss).isNotNull();
		Screenshot ss2 = ssRepo.findByScreenshotID(ss.getScreenshotID());
		assertThat(ss2).isNotNull();
		assertTrue(ss2.getScreenshotName().equals("Screenshot100"));
		Screenshot ss3 = ssRepo.findByScreenshotID(101);
		assertEquals(ss3, null);
	}

	@BeforeEach
	private void putScreenshotsToDB() {
		uRepo.save(new UserDTO(ulRepo.findByValue("USER"), "username", "password", "password"));
		uRepo.save(new UserDTO(ulRepo.findByValue("USER"), "username2", "password", "password"));
		ssRepo.save(new Screenshot(uRepo.findByUsername("username"), "Screenshot1", ""));
		ssRepo.save(new Screenshot(uRepo.findByUsername("username"), "Screenshot2", ""));
		Screenshot ss = new Screenshot(uRepo.findByUsername("username2"), "Screenshot100", "");
		ssRepo.save(ss);
	}

}
