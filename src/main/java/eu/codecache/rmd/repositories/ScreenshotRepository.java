package eu.codecache.rmd.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;

public interface ScreenshotRepository extends JpaRepository<Screenshot, Long> {

	Screenshot findByScreenshotID(long screenshotID);
	Screenshot findByScreenshotName(String screenshotName);
	
	// We need to be able to find screenshots by user
	List<Screenshot> findByUser(UserDTO user);
}
