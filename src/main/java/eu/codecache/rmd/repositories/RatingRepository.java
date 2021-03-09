package eu.codecache.rmd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eu.codecache.rmd.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

	// We need to check how to add parameters to the query ?1 didn't work...
	@Query(value="SELECT AVG(r.rating) FROM Rating r WHERE r.screenshot=6")
	public double avg(Long screenshotID);
}
