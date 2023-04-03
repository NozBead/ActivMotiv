package eu.euromov.activmotiv.controller;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@Autowired
	PasswordEncoder encoder;
	
	@PostMapping(consumes = "application/json")
	public ResponseEntity<?> create(@Valid @RequestBody Participant participant) {
		Optional<Participant> found = participants.findById(participant.getUsername());
		// The given username needs to be already added without a password to be created
		if (found.isEmpty()) {
			log.info("Unauthorized ID submitted to registration: " + participant.getUsername());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		else if (found.get().getPassword() != null) {

			log.info("Already existing ID submitted to registration: " + participant.getUsername());
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		
		participant.setPassword(encoder.encode(participant.getPassword()));
		participants.save(participant);
		
		log.info("Registered user: " + participant.getUsername());
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/login")
	public void login() {}
}