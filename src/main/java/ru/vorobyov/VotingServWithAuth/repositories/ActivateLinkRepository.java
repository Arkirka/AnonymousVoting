package ru.vorobyov.VotingServWithAuth.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;

@Repository
public interface ActivateLinkRepository extends CrudRepository<ActivateLink, Integer> {
    ActivateLink findActivateLinkByLink(String link);
}
