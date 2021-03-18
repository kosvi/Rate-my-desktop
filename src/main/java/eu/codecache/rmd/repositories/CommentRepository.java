package eu.codecache.rmd.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import eu.codecache.rmd.model.Comment;
import eu.codecache.rmd.model.Screenshot;

public interface CommentRepository extends CrudRepository<Comment, Long> {

	List<Comment> findByScreenshot(Screenshot screenshot);

	Comment findByCommentID(long commentID);
}
