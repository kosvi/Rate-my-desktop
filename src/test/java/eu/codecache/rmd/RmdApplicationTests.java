package eu.codecache.rmd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import eu.codecache.rmd.rest.CommentController;
import eu.codecache.rmd.rest.RatingController;
import eu.codecache.rmd.rest.ScreenshotController;
import eu.codecache.rmd.rest.UserController;
import eu.codecache.rmd.web.WebController;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RmdApplicationTests {

	// Let's make smoketests to all controllers
	@Autowired
	private WebController webController;
	@Autowired
	private CommentController commentController;
	@Autowired
	private RatingController ratingController;
	@Autowired
	private ScreenshotController screenshotController;
	@Autowired
	private UserController userController;

	/*
	 * Test that all controllers are loaded
	 */
	@Test
	void contextLoads() throws Exception {
		assertThat(webController).isNotNull();
		assertThat(commentController).isNotNull();
		assertThat(ratingController).isNotNull();
		assertThat(screenshotController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
