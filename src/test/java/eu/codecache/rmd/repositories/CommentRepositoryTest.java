package eu.codecache.rmd.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import eu.codecache.rmd.model.Comment;
import eu.codecache.rmd.model.Rating;
import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.model.UserLevel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CommentRepositoryTest {

	@Autowired
	private CommentRepository cRepo;

	/*
	 * We will also need these repositories to be able to user cRepo
	 */
	@Autowired
	private UserRepository uRepo;
	@Autowired
	private UserLevelRepository ulRepo;
	@Autowired
	private ScreenshotRepository ssRepo;

	@Test
	public void listCommentsTest() {
		List<Comment> comments = cRepo.findByScreenshot(ssRepo.findByScreenshotName("Screenshot1"));
		assertEquals(comments.size(), 2);
		assertTrue(comments.get(0).getComment().equals("Nice pic!"));
	}

	@Test
	public void addCommentTest() {
		// the @BeforeEach kinda tests this already don't it?

		// Let's add a new user
		UserDTO user = uRepo.save(new UserDTO(ulRepo.findByValue("USER"), "username2", "password", "password"));
		// And save comment from him/her
		cRepo.save(new Comment(ssRepo.findByScreenshotName("Screenshot2"), user, "Testing..."));
	}

	@BeforeEach
	private void addCommentsToDB() {
		UserDTO user = uRepo.save(new UserDTO(ulRepo.findByValue("USER"), "username", "password", "password"));
		Screenshot ss1 = ssRepo.save(new Screenshot(uRepo.findByUsername("username"), "Screenshot1", ""));
		Screenshot ss2 = ssRepo.save(new Screenshot(uRepo.findByUsername("username"), "Screenshot2", ""));
		cRepo.save(new Comment(ss1, user, "Nice pic!"));
		cRepo.save(new Comment(ss1, user, "I like it alot!"));
	}
}
