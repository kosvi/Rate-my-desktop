package eu.codecache.rmd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UserDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userID;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "levelID")
	private UserLevel level;

	@Size(min = 3, max = 100)
	private String username;

	@Size(min = 5, max = 200)
	private String password;
	private String password2;

	private String passwordHash;

	public boolean encodePassword() {
		if (password.equals(password2)) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			passwordHash = encoder.encode(password);
			password = "123456789";
			password2 = "987654321";
			return true;
		}
		return false;
	}

	public UserDTO() {

	}

	public UserDTO(UserLevel level, String username) {
		this.level = level;
		this.username = username;
	}

	public UserDTO(UserLevel level, String username, String passwordHash) {
		this(level, username);
		this.passwordHash = passwordHash;
	}

	public UserDTO(UserLevel level, String username, String password, String password2) {
		this(level, username);
		this.password = password;
		this.password2 = password2;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public UserLevel getLevel() {
		return level;
	}

	public void setLevel(UserLevel level) {
		this.level = level;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password) {
		this.password2 = password;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
