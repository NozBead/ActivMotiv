package eu.euromov.activmotiv.repository;

import org.springframework.data.repository.CrudRepository;

import eu.euromov.activmotiv.model.Participant;

public interface ParticipantRepository extends CrudRepository<Participant, Integer> {
	
}
