package eu.codecache.rmd.web;

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
		if(existing!=null) {
			if(existing.getUsername().equalsIgnoreCase(userDTO.getUsername())) {
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
