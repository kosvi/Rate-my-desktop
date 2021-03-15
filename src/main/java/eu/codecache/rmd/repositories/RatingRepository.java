package eu.codecache.rmd.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eu.codecache.rmd.model.Rating;
import eu.codecache.rmd.model.Screenshot;
import eu.codecache.rmd.model.UserDTO;

public interface RatingRepository extends JpaRepository<Rating, Long> {

	// We need to be able to fetch the rating given for the screenshot
	@Query(value="SELECT AVG(r.rating) FROM Rating r WHERE r.screenshot=?1")
	public double avg(Screenshot ss);
	
	// We need to fetch rating by screenshot & user
	@Query(value="SELECT r FROM Rating r WHERE r.screenshot=?1 AND r.user=?2")
	public Rating userRating(Screenshot ss, UserDTO user);
	
	// list ratings by screenshot (is this needed anywhere?)
	public List<Rating> findRatingsByScreenshot(Screenshot ss);
}
