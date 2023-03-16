package eu.euromov.activmotiv.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.euromov.activmotiv.model.Unlock;
import eu.euromov.activmotiv.repository.UnlockRepository;

@RestController
@RequestMapping(path="/unlock")
public class UnlockController {
	
	private Log log = LogFactory.getLog(UnlockController.class);
	
	@Autowired
	UnlockRepository unlocks;
	
	@PostMapping
	public void createUnlock(@RequestBody Unlock unlock) {
		unlocks.save(unlock);
	}
	
}