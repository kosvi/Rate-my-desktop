package eu.codecache.rmd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eu.codecache.rmd.model.Rating;
import eu.codecache.rmd.model.Screenshot;

public interface RatingRepository extends JpaRepository<Rating, Long> {

	// We need to be able to fetch the rating given for the screenshot
	@Query(value="SELECT AVG(r.rating) FROM Rating r WHERE r.screenshot=?1")
	public double avg(Screenshot ss);
}
