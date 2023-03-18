package eu.euromov.activmotiv.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.euromov.activmotiv.model.Participant;
import eu.euromov.activmotiv.repository.ParticipantRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path="/participant")
public class ParticipantController {
	
	private Log log = LogFactory.getLog(ParticipantController.class);
	
	@Autowired
	ParticipantRepository participants;
	
	@PostMapping
	public ResponseEntity<?> createParticipant(@Valid @RequestBody Participant participant) {
		if (participants.existsById(participant.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		participant.setPassword(encoder.encode(participant.getPassword()));
		participants.save(participant);
		return ResponseEntity.ok().build();
	}
	
}