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
	private String name;

	@Size(min = 3, max = 100)
	private String value;

	public UserLevel() {

	}

	public UserLevel(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public long getLevelID() {
		return levelID;
	}

	public void setLevelID(long levelID) {
		this.levelID = levelID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
