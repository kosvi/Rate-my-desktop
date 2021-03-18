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

	/*
	 * This endpoint returns current users rating for current screenshot If user is
	 * not logged in, rating value 0 is returned with empty user.
	 */
	@RequestMapping(value = API_BASE + "/{id}", method = RequestMethod.GET)
	public @ResponseBody Rating getRating(@PathVariable("id") Long screenshotID, Principal principal) {
		// This is how I check login at the moment
		if (principal == null) {
			// if no screenshot with that id is found, null is returned (and that is fine
			// for us!)
			return this.defaultRating(screenshotID);
		}
		// If screenshotID is invalid or we have other issues in fetching data, we will
		// return default data (instead of for example 404)
		try {
			return rRepo.userRating(ssRepo.findByScreenshotID(screenshotID), uRepo.findByUsername(principal.getName()));
		} catch (Exception e) {
			return this.defaultRating(screenshotID);
		}
	}

	@RequestMapping(value = API_BASE + "/rate/{id}", method = RequestMethod.GET)
	public @ResponseBody Rating addRating(@PathVariable("id") Long screenshotID, @RequestParam String newValue,
			Principal principal) {
		// First let's make sure user is logged in!
		if (principal == null) {
			// I wonder if this is enough??
			return null;
		}
		// now, let's fetch the rating if there is already one for this user &
		// screenshot
		Screenshot ss = ssRepo.findByScreenshotID(screenshotID);
		UserDTO user = uRepo.findByUsername(principal.getName());
		Rating current = rRepo.userRating(ss, user);
		// check if current is null
		if (current == null) {
			current = new Rating(ss, user);
		}
		// take given rating from params
		int newRating = 0;
		try {
			newRating = Integer.valueOf(newValue);
		} catch (Exception e) {
			// value given wasn't integer
			newRating = -1;
		}
		if (newRating > 0 && newRating < 6) {
			// rating was valid, let's update repository
			current.setRating(newRating);
			return rRepo.save(current);
		}
		return new Rating(ss, uRepo.findByUsername(principal.getName()), newRating);
	}

	/*
	 * This will return "default" rating (value 0, empty user)
	 */
	private Rating defaultRating() {
		return defaultRating(0);
	}

	private Rating defaultRating(long screenshotID) {
		return new Rating(ssRepo.findByScreenshotID(screenshotID), new UserDTO(), 0);
	}

}
