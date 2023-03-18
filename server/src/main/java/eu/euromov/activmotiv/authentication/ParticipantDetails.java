package eu.euromov.activmotiv.authentication;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import eu.euromov.activmotiv.model.Participant;

public class ParticipantDetails implements UserDetails {
	private Participant participant;
	
	public ParticipantDetails(Participant participant) {
		this.participant = participant;
	}
	
	public Participant getParticipant() {
		return participant;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return participant.getPassword();
	}

	@Override
	public String getUsername() {
		return participant.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
