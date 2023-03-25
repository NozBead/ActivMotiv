package eu.euromov.activmotiv.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.euromov.activmotiv.model.Participant;
import eu.euromov.activmotiv.model.Unlock;
import eu.euromov.activmotiv.model.UnlockId;
import eu.euromov.activmotiv.repository.UnlockRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/unlock")
public class UnlockController {
	
	private Log log = LogFactory.getLog(UnlockController.class);
	
	@Autowired
	UnlockRepository unlocks;
	
	@PostMapping
	public void createUnlock(@Valid @RequestBody Unlock unlock, Authentication auth) {
		Jwt token = (Jwt) auth.getPrincipal();
		String username = token.getClaimAsString("participant");
		log.info("Unlock from " + username);
		
		Participant participant = new Participant();
		participant.setUsername(username);
		UnlockId id = new UnlockId();
		id.setParticipant(participant);
		id.setTime(unlock.getTime());
		if (!unlocks.existsById(id)) {
			unlock.setParticipant(participant);
			unlocks.save(unlock);
		}
	}
	
}