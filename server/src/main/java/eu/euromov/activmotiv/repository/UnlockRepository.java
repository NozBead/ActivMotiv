package eu.euromov.activmotiv.repository;

import org.springframework.data.repository.CrudRepository;

import eu.euromov.activmotiv.model.Unlock;
import eu.euromov.activmotiv.model.UnlockId;

public interface UnlockRepository extends CrudRepository<Unlock, UnlockId> {
	
}
