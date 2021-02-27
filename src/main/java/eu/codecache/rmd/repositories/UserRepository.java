package eu.codecache.rmd.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.codecache.rmd.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findByUsername(String username);

}
