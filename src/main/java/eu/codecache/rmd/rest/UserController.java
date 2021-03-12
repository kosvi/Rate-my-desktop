package eu.codecache.rmd.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.repositories.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserRepository uRepo;

	private final String API_BASE = "/api/user";

	@RequestMapping(value = API_BASE, method = RequestMethod.POST)
	public @ResponseBody UserDTO updatePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("password") String password, @RequestParam("password2") String password2,
			Principal principal) {
		UserDTO user = uRepo.findByUsername(principal.getName());
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		if (this.changeUserPassword(user, oldPassword, password)) {
			// success!
			return uRepo.findByUsername(principal.getName());
		}
		// errorhandling has to be better!
		return new UserDTO();
	}

	private boolean changeUserPassword(UserDTO user, String oldPassword, String newPassword) {
		if (this.checkOldPassword(user, oldPassword)) {
			// old password ok -> we can update new password
			user.setPassword(newPassword);
			user.setPassword2(newPassword);
			if (user.encodePassword()) {
				uRepo.save(user);
				return true;
			}
		}
		return false;
	}

	private boolean checkOldPassword(UserDTO user, String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, user.getPasswordHash());
	}

}
