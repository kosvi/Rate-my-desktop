package eu.codecache.rmd.repositories;

import org.springframework.data.repository.CrudRepository;

import eu.codecache.rmd.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
