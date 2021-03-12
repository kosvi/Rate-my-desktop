package eu.codecache.rmd.rest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.codecache.rmd.repositories.CommentRepository;
import eu.codecache.rmd.repositories.ScreenshotRepository;
import eu.codecache.rmd.repositories.UserRepository;
import eu.codecache.rmd.model.Comment;
import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;

@RestController
public class CommentController {

	private static final String API_BASE = "/api/comments";

	@Autowired
	private CommentRepository cRepo;

	@Autowired
	private ScreenshotRepository ssRepo;

	@Autowired
	private UserRepository uRepo;

	/*
	 * This method is for adding new comments. Required fields are: "comment":
	 * "users comments" (max 255 characters)
	 */
	@RequestMapping(value = API_BASE + "/{id}", method = RequestMethod.POST)
	public @ResponseBody List<Comment> postComment(@PathVariable("id") Long screenshotID, @RequestBody Comment comment,
			Principal principal) {
		if (principal == null) {
			// user is not logged in -> return failure
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "");
		} else {
			// Add user, I guess it will be null if for some strange reason
			// the user is not found from repository
			comment.setUser(uRepo.findByUsername(principal.getName()));
		}
		Screenshot ss = ssRepo.findByScreenshotID(screenshotID);
		if (ss != null) {
			// We found the screenshot, we are ready to store data
			comment.setScreenshot(ss);
			comment.setTimestampToCurrent();
			cRepo.save(comment);
		}
		return cRepo.findByScreenshot(ss);
	}

	/*
	 * This method is for ADMIN ONLY! It's for deleting individual comments
	 */
	@RequestMapping(value = API_BASE + "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody List<Comment> deleteComment(@PathVariable("id") Long commentID, Principal principal) {
		UserDTO user = uRepo.findByUsername(principal.getName());
		if (user != null && user.getLevel().getValue().equals("ADMIN")) {
			// this is the minimum to even get started!
			Comment comment = cRepo.findByCommentID(commentID);
			if (comment != null) {
				// comment with ID was found -> delete and return list of remaining comments
				Screenshot screenshot = comment.getScreenshot();
				cRepo.delete(comment);
				return cRepo.findByScreenshot(screenshot);
			}
			// comment wasn't found with ID -> 404
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		// User is not admin (or logged in at all!) -> 403
		throw new ResponseStatusException(HttpStatus.FORBIDDEN);
	}
}
