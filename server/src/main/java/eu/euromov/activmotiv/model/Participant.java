package eu.euromov.activmotiv.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Participant {
	@Id
	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
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

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participant other = (Participant) obj;
		return Objects.equals(username, other.username);
	}
}
