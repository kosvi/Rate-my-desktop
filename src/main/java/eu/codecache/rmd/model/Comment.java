package eu.codecache.rmd.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long commentID;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "screenshotID")
	private Screenshot screenshot;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "userID")
	private UserDTO user;

	@Size(min = 1, max = 255)
	private String comment;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public Comment() {

	}

	public Comment(Screenshot screenshot, UserDTO user, String comment) {
		this.screenshot = screenshot;
		this.user = user;
		this.comment = comment;
		this.timestamp = new Date();
	}

	public long getCommentID() {
		return commentID;
	}

	public void setCommentID(long commentID) {
		this.commentID = commentID;
	}

	public Screenshot getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(Screenshot screenshot) {
		this.screenshot = screenshot;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
