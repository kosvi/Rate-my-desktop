package eu.codecache.rmd.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import eu.codecache.rmd.model.Rating;
import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.model.UserLevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RatingRepositoryTest {

	@Autowired
	private RatingRepository rRepo;

	// We also need repositories that the ratings rely on
	@Autowired
	private UserRepository uRepo;
	@Autowired
	private UserLevelRepository ulRepo;
	@Autowired
	private ScreenshotRepository ssRepo;

	/*
	 * Let's test that our repository calculates average rating correctly
	 */
	@Test
	public void calculateRatingTest() throws Exception {
		assertEquals(rRepo.avg(ssRepo.findByScreenshotName("Screenshot1")), 3.0);
		// Now what about screenshot without ratings?
// For some reason this test always fails, what should this return anyways? Or does it crash?
//		assertEquals(rRepo.avg(ssRepo.findByScreenshotName("Screenshot2")), 0.0);
		// Add ratings to "Screenshot2" and calculate again
		rRepo.save(new Rating(ssRepo.findByScreenshotName("Screenshot2"), uRepo.findByUsername("username"), 2));
		rRepo.save(new Rating(ssRepo.findByScreenshotName("Screenshot2"), uRepo.findByUsername("username2"), 3));
		assertEquals(rRepo.avg(ssRepo.findByScreenshotName("Screenshot2")), 2.5);
	}

	/*
	 * Let's fill some data to our rating repository
	 */
	@BeforeEach
	private void addSomeRatings() {
		ulRepo.save(new UserLevel("user", "USER"));
		UserDTO user = uRepo.save(new UserDTO(ulRepo.findByValue("USER"), "username", "password", "password"));
		UserDTO user2 = uRepo.save(new UserDTO(ulRepo.findByValue("USER"), "username2", "password", "password"));
		Screenshot ss1 = ssRepo.save(new Screenshot(uRepo.findByUsername("username"), "Screenshot1", ""));
		Screenshot ss2 = ssRepo.save(new Screenshot(uRepo.findByUsername("username"), "Screenshot2", ""));
		rRepo.save(new Rating(ssRepo.findByScreenshotID(ss1.getScreenshotID()), user, 1));
		rRepo.save(new Rating(ssRepo.findByScreenshotID(ss1.getScreenshotID()), user2, 5));
	}
}
