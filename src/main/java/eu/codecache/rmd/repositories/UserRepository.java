package eu.codecache.rmd.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.codecache.rmd.model.UserDTO;

public interface UserRepository extends CrudRepository<UserDTO, Long> {
	
	UserDTO findByUsername(String username);

}
