package eu.codecache.rmd.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import eu.codecache.rmd.model.Screenshot;

public interface ScreenshotRepository extends JpaRepository<Screenshot, Long> {

}
