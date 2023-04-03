package eu.euromov.activmotiv.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.euromov.activmotiv.authentication.ParticipantDetails;
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
	public void createUnlock(@Valid @RequestBody Unlock unlock, UsernamePasswordAuthenticationToken token) {
		ParticipantDetails participant = (ParticipantDetails) token.getPrincipal();
		
		UnlockId id = new UnlockId();
		id.setParticipant(participant.getParticipant());
		id.setTime(unlock.getTime());
		if (!unlocks.existsById(id)) {
			unlock.setParticipant(participant.getParticipant());
			unlocks.save(unlock);
			log.info("Unlock from " + participant.getUsername() + " added");
		}
		else {
			log.info("Unlock from " + participant.getUsername() + " already existing");
		}
	}
	
}