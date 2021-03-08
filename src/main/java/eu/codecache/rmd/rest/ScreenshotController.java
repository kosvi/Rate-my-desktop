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

import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.repositories.ScreenshotRepository;
import eu.codecache.rmd.repositories.UserRepository;

@RestController
public class ScreenshotController {

	@Autowired
	private ScreenshotRepository ssRepo;

	@Autowired
	private UserRepository uRepo;

	private final String API_BASE = "/api/screenshots";

	/*
	 * Simply fetch ALL screenshots from database and return one random line from
	 * the list
	 */
	@RequestMapping(value = API_BASE + "/random", method = RequestMethod.GET)
	public @ResponseBody Screenshot randomScreenshot() {
		List<Screenshot> screenshots = this.getScreenshots();
		Random r = new Random();
		int randomIndex = r.nextInt(screenshots.size());
		return screenshots.get(randomIndex);
	}

	@RequestMapping(value = API_BASE + "/{id}", method = RequestMethod.GET)
	public @ResponseBody Screenshot getScreenshot(@PathVariable("id") Long screenshotID) {
		/*
		 * For now we don't handle the situation where ID isn't found from database.
		 * (added as issue on GitHub)
		 */
		Screenshot ss = ssRepo.findByScreenshotID(screenshotID);
		return ss;
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
