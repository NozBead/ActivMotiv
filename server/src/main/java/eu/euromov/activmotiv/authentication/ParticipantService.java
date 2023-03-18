package eu.euromov.activmotiv.authentication;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import eu.euromov.activmotiv.model.Participant;
import eu.euromov.activmotiv.repository.ParticipantRepository;

public class ParticipantService implements UserDetailsService {
	
	@Autowired
	ParticipantRepository participants;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Participant> p = participants.findById(username);
		if (p.isEmpty()) {
			throw new UsernameNotFoundException(username);
		}
		
		return new ParticipantDetails(p.get());
	}
}
