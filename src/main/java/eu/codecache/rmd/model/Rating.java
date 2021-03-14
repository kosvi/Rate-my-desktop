package eu.codecache.rmd.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Rating {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long ratingID;

	@NotNull
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "screenshotID")
	private Screenshot screenshot;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "userID")
	private UserDTO user;

//	@NotNull
//	@Min(1)
//	@Max(5)
	private int rating;

	public Rating() {

	}

	public Rating(Screenshot screenshot, UserDTO user) {
		super();
		this.screenshot = screenshot;
		this.user = user;
	}

	public Rating(Screenshot screenshot, UserDTO user, int rating) {
		this(screenshot, user);
		this.rating = rating;
	}

	public long getRatingID() {
		return ratingID;
	}

	public void setRatingID(long ratingID) {
		this.ratingID = ratingID;
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

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

}
