package eu.codecache.rmd.web;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.repositories.ScreenshotRepository;
import eu.codecache.rmd.repositories.UserLevelRepository;
import eu.codecache.rmd.repositories.UserRepository;

@Controller
public class WebController {

	// We need this to be able to store registered users to database
	@Autowired
	private UserRepository uRepo;
	@Autowired
	private UserLevelRepository ulRepo;
	@Autowired
	private ScreenshotRepository ssRepo;

	@Value("${upload.path}")
	private String UPLOAD_FOLDER;

	// Screenshots are found from the root. Username is send to model
	// if user is logged in
	@GetMapping("/")
	public String screenshot(Model model, Principal principal) {
		model.addAttribute("ssID", 0);
		if (principal != null) {
			model.addAttribute("name", principal.getName());
			model.addAttribute("loggedIn", true);
		} else {
			model.addAttribute("name", false);
			model.addAttribute("loggedIn", false);
		}
		return "screenshot";
	}

	// and if we add id to path, we get specific screenshot
	// (for logged in users only)
	@GetMapping("/{id}")
	public String screenshotByID(@PathVariable("id") Long screenshotID, Model model, Principal principal) {
		if (principal != null) {
			model.addAttribute("ssID", screenshotID);
			model.addAttribute("name", principal.getName());
			model.addAttribute("loggedIn", true);
			return "screenshot";
		} else {
			return "redirect:/login";
		}
	}

	// We also want to serve the profile page to change password & upload
	// screenshots
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		String username = principal.getName();
		UserDTO user = uRepo.findByUsername(username);
		if (user != null) {
			// we got a user, that's great!
			model.addAttribute("username", user.getUsername());
		} else {
			// I don't know why we didn't get a user from database
			// since this method should never be run unless the
			// maker of the request is logged in!
			model.addAttribute("username", false);
		}
		return "profile";
	}

	/*
	 * Still quite some work to be done here!
	 */
	@PostMapping("/upload")
	public String uploadScreenshot(@RequestParam("file") MultipartFile file, @RequestParam("name") String name,
			Principal principal, Model model) {
		// this attribute is used to check if we need to show the message or not
		model.addAttribute("upload", true);
		if (file.isEmpty()) {
			// handle case, where upload failed
			model.addAttribute("message", "Upload failed");
			return "profile";
		}
		// this is a magic number, let's fix that later... Im so lazy :)
		if (name.length() < 5) {
			model.addAttribute("message", "Name is too short");
			return "profile";
		}
		// save screenshot to repository in order to get an ID for it
		Screenshot ss = new Screenshot(uRepo.findByUsername(principal.getName()), name, "");
		Screenshot newSS = ssRepo.save(ss);
		try {
			byte[] bytes = file.getBytes();
			// here we use the id of the just added screenshot to generate a filename for it
			Path path = Paths.get(this.createScreenshotFilename(newSS.getScreenshotID()));
			Files.write(path, bytes);
			model.addAttribute("message", "Upload successfull");
		} catch (Exception e) {
			// we need to handle error, more technological debt thank you sir
			model.addAttribute("message", "Upload failed");
		}
		return "profile";
	}

	/*
	 * After image upload, we want to be able to show it too...
	 * https://stackoverflow.com/questions/62825338/how-to-send-image-as-response-in
	 * -spring-boot
	 * 
	 * Source of pretty much just copy&pasted method
	 */
	@GetMapping(value = "/pics/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<Resource> pngScreenshot(@PathVariable("id") Long screenshotID) throws Exception {
//		final String pathToImageData = ssRepo.findByScreenshotID(screenshotID).getFilename();
		final String pathToImageData = this.createScreenshotFilename(screenshotID);
		try {
			final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(pathToImageData)));
			return ResponseEntity.status(HttpStatus.OK).contentLength(inputStream.contentLength()).body(inputStream);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	// This is just a test path, it shouldn't cause harm in production
	// so let's keep it for now
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	/*
	 * This path will show the registration form
	 */
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute(new UserDTO());
		return "register";
	}

	/*
	 * and after filling the form, we will register the user to our database
	 */
	@PostMapping("/register")
	public String registerSave(@Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
		// if userDTO is not valid -> back to form
		if (bindingResult.hasErrors()) {
			return "register";
		}
		// Now we want to see if the username is already in use
		UserDTO existing = uRepo.findByUsername(userDTO.getUsername());
		if (existing != null) {
			if (existing.getUsername().equalsIgnoreCase(userDTO.getUsername())) {
				// this username already exists in the database
				model.addAttribute("nameTaken", true);
				return "register";
			}
		}
		// if the user gave valid username & password, let's continue
		if (userDTO.encodePassword()) {
			// this returned true if we successfully encoded the password,
			// it fails it password1 and password2 don't match
			userDTO.setLevel(ulRepo.findByValue("USER"));
			uRepo.save(userDTO);
			return "redirect:/hello";
		}
		model.addAttribute("passwordNoMatch", true);
		return "register";
	}

	/*
	 * generate name for screenshot when you know the ID
	 */
	private String createScreenshotFilename(long id) {
		return UPLOAD_FOLDER + id + ".data";
	}
}
