package eu.codecache.rmd.rest;

import java.security.Principal;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.codecache.rmd.model.Comment;
import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.repositories.CommentRepository;
import eu.codecache.rmd.repositories.RatingRepository;
import eu.codecache.rmd.repositories.ScreenshotRepository;
import eu.codecache.rmd.repositories.UserRepository;

@RestController
public class ScreenshotController {

	/*
	 * This is nasty and dirty, but let's make it so for now
	 */
	private class SingleShot {
		private long id;
		private String name;
		private double rating;
		private List<Comment> comments;

		public SingleShot(Screenshot ss, double r, List<Comment> c) {
			this.id = ss.getScreenshotID();
			this.name = ss.getScreenshotName();
			this.rating = r;
			this.comments = c;
		}

		public SingleShot(long id, String n, double r, List<Comment> c) {
			this.id = id;
			this.name = n;
			this.rating = r;
			this.comments = c;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getRating() {
			return rating;
		}

		public void setRating(double rating) {
			this.rating = rating;
		}

		public List<Comment> getComments() {
			return comments;
		}

		public void setComments(List<Comment> comments) {
			this.comments = comments;
		}
	}

	@Autowired
	private ScreenshotRepository ssRepo;

	@Autowired
	private RatingRepository rRepo;

	@Autowired
	private UserRepository uRepo;

	@Autowired
	private CommentRepository cRepo;

	private final String API_BASE = "/api/screenshots";

	/*
	 * Simply fetch ALL screenshots from database and return one random line from
	 * the list
	 */
	@RequestMapping(value = API_BASE + "/random", method = RequestMethod.GET)
	public @ResponseBody SingleShot randomScreenshot() {
		List<Screenshot> screenshots = this.getScreenshots();
		Random r = new Random();
		int randomIndex = r.nextInt(screenshots.size());
		double rating = -1;
		try {
			rating = rRepo.avg(screenshots.get(randomIndex));
		} catch (Exception e) {
			// no ratings set yet, we don't have to handle it any better
		}
		List<Comment> comments = cRepo.findByScreenshot(screenshots.get(randomIndex));
		return new SingleShot(screenshots.get(randomIndex), rating, comments);
	}

	@RequestMapping(value = API_BASE + "/{id}", method = RequestMethod.GET)
	public @ResponseBody SingleShot getScreenshot(@PathVariable("id") Long screenshotID) {
		/*
		 * For now we don't handle the situation where ID isn't found from database.
		 * (added as issue on GitHub)
		 */
		Screenshot ss = ssRepo.findByScreenshotID(screenshotID);
		double rating = rRepo.avg(ss);
		List<Comment> comments = cRepo.findByScreenshot(ss);
		return new SingleShot(ss.getScreenshotID(), ss.getScreenshotName(), rating, comments);
	}

	@RequestMapping(value = API_BASE, method = RequestMethod.GET)
	public @ResponseBody List<Screenshot> listAllScreenshots() {
		return this.getScreenshots();
	}

	private List<Screenshot> getScreenshots() {
		List<Screenshot> screenshots = ssRepo.findAll();
		return screenshots;
	}

	@RequestMapping(value = API_BASE + "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Screenshot deleteScreenshot(@PathVariable("id") Long screenshotID, Principal principal) {
		// Let's see if the user is logged in
		UserDTO user = uRepo.findByUsername(principal.getName());
		Screenshot ss = ssRepo.findByScreenshotID(screenshotID);
		if (user.getUserID() == ss.getUser().getUserID()) {
			// User is the owner
			ssRepo.delete(ss);
		} else {
			return ss;
		}
		return new Screenshot(user, "Deleted!!!", "Deleted!!!");
	}
}
