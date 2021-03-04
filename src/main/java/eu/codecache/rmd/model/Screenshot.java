package eu.codecache.rmd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Screenshot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long screenshotID;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "userID")
	private UserDTO user;

	@Size(min = 5, max = 100)
	private String screenshotName;

	@JsonIgnore
	@Size(min = 10, max = 100)
	private String filename;

	public Screenshot() {

	}

	public Screenshot(UserDTO user, String name, String filename) {
		super();
		this.user = user;
		this.screenshotName = name;
		this.filename = filename;
	}

	public long getScreenshotID() {
		return screenshotID;
	}

	public void setScreenshotID(long screenshotID) {
		this.screenshotID = screenshotID;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public String getScreenshotName() {
		return screenshotName;
	}

	public void setScreenshotName(String screenshotName) {
		this.screenshotName = screenshotName;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	
}
