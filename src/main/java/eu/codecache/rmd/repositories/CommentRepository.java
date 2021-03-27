package eu.codecache.rmd.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import eu.codecache.rmd.model.Comment;
import eu.codecache.rmd.model.Screenshot;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByScreenshot(Screenshot screenshot);

	Comment findByCommentID(long commentID);
}
