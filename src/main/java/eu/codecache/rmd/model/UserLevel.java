package eu.codecache.rmd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class UserLevel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long levelID;

	@Size(min = 3, max = 100)
	private String levelName;

	@Size(min = 3, max = 100)
	private String levelValue;

	public UserLevel() {

	}

	public UserLevel(String name, String value) {
		super();
		this.levelName = name;
		this.levelValue = value;
	}

	public long getLevelID() {
		return levelID;
	}

	public void setLevelID(long levelID) {
		this.levelID = levelID;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLevelValue() {
		return levelValue;
	}

	public void setLevelValue(String levelValue) {
		this.levelValue = levelValue;
	}
}
