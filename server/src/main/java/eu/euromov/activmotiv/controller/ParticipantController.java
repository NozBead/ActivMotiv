package eu.euromov.activmotiv.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.euromov.activmotiv.model.Participant;
import eu.euromov.activmotiv.repository.ParticipantRepository;

@RestController
@RequestMapping(path="/participant")
public class ParticipantController {
	
	private Log log = LogFactory.getLog(ParticipantController.class);
	
	@Autowired
	ParticipantRepository participants;
	
	@PostMapping
	public void createParticipant(@RequestBody Participant participant) {
		participants.save(participant);
	}
}