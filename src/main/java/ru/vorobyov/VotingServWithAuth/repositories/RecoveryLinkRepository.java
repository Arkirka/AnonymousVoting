package ru.vorobyov.VotingServWithAuth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.Optional;

@Repository
public interface RecoveryLinkRepository extends JpaRepository<RecoveryLink, Integer> {
    Optional<RecoveryLink> findRecoveryLinkByLink(String link);
    Optional<RecoveryLink> findRecoveryLinkByUser(User user);
}
