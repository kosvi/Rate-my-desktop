package eu.codecache.rmd.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eu.codecache.rmd.model.UserLevel;

public interface UserLevelRepository extends CrudRepository<UserLevel, Long> {
	List<UserLevel> findByValue(String value);
}
