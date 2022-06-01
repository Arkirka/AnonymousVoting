package ru.vorobyov.VotingServWithAuth.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.List;

@Repository
public interface RecoveryLinkRepository extends CrudRepository<RecoveryLink, Integer> {
    RecoveryLink findRecoveryLinkByLink(String link);
    List<RecoveryLink> findRecoveryLinksByUser(User user);
}
