package eu.codecache.rmd.web;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.repositories.UserLevelRepository;
import eu.codecache.rmd.repositories.UserRepository;

@Controller
public class WebController {

	// We need this to be able to store registered users to database
	@Autowired
	private UserRepository uRepo;
	@Autowired
	private UserLevelRepository ulRepo;

	// Screenshots are found from the root. Username is send to model
	// if user is logged in
	@GetMapping("/")
	public String screenshot(Model model, Principal principal) {
		if (principal != null) {
			model.addAttribute("name", principal.getName());
		} else {
			model.addAttribute("name", false);
		}
		return "screenshot";
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
}
