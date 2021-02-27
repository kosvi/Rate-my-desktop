package eu.codecache.rmd.rest;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.repositories.ScreenshotRepository;

@RestController
public class ScreenshotController {

	@Autowired
	private ScreenshotRepository ssRepo;

	@RequestMapping(value="/api/random", method=RequestMethod.GET)
	public @ResponseBody Screenshot randomScreenshot() {
		List<Screenshot> screenshots = ssRepo.findAll();
		Random r = new Random();
		int randomIndex = r.nextInt(screenshots.size());
		return screenshots.get(randomIndex);
	}
}
