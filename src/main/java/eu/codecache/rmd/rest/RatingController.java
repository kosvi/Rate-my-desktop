package eu.codecache.rmd.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.codecache.rmd.model.Rating;
import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.repositories.RatingRepository;
import eu.codecache.rmd.repositories.ScreenshotRepository;
import eu.codecache.rmd.repositories.UserRepository;

@RestController
public class RatingController {

	private final String API_BASE = "/api/ratings";

	@Autowired
	RatingRepository rRepo;

	@Autowired
	ScreenshotRepository ssRepo;

	@Autowired
	UserRepository uRepo;

	@RequestMapping(value = API_BASE + "/{id}", method = RequestMethod.GET)
	public @ResponseBody Rating getRatings(@PathVariable("id") Long screenshotID, Principal principal) {
		if (principal == null) {
			return new Rating(ssRepo.findByScreenshotID(screenshotID), new UserDTO(), 0);
		}
		try {
			return rRepo.userRating(ssRepo.findByScreenshotID(screenshotID), uRepo.findByUsername(principal.getName()));
		} catch (Exception e) {
			return new Rating(ssRepo.findByScreenshotID(screenshotID), new UserDTO(), 0);
		}
//		return this.getRatingsFromDB(screenshotID);
	}

	@RequestMapping(value = API_BASE + "/rate/{id}", method = RequestMethod.GET)
	public @ResponseBody Rating addRating(@PathVariable("id") Long screenshotID, @RequestParam String newValue, Principal principal) {
		// First let's make sure user is logged in!
		if (principal == null) {
			// I wonder if this is enough??
			return null;
		}
		// now, let's fetch the screenshot
		Screenshot ss = ssRepo.findByScreenshotID(screenshotID);
		int newRating = 0;
		try {
			newRating = Integer.valueOf(newValue);
		} catch (Exception e) {
			// value given wasn't integer
			newRating = -1;
		}
		if (newRating > 0 && newRating < 6) {
			// rating was valid, let's update repository
			// TODO here
		}
		return new Rating(ss, uRepo.findByUsername(principal.getName()), newRating);
	}

	/*
	 * Now we need to fix this method to handle possible error conditions!
	 */
	private List<Rating> getRatingsFromDB(Long screenshotID) {
		Screenshot ss = ssRepo.findByScreenshotID(screenshotID);
		return rRepo.findRatingsByScreenshot(ss);
	}

}
