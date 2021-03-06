package eu.codecache.rmd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import eu.codecache.rmd.model.UserDTO;
import eu.codecache.rmd.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private final UserRepository uRepo;

	@Autowired
	public MyUserDetailsService(UserRepository userRepository) {
		uRepo = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDTO user = uRepo.findByUsername(username);
		/*
		 * I didn't check if user was found and I ended up getting strage error-messages
		 * when mistyping username to login form. This fixed those issues.
		 */
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		UserDetails details = new User(user.getUsername(), user.getPasswordHash(),
				AuthorityUtils.createAuthorityList(user.getLevel().getValue()));
		return details;
	}
}
