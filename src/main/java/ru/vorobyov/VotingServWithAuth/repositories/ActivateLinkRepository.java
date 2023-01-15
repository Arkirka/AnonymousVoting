package ru.vorobyov.VotingServWithAuth.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;

import java.util.Optional;

@Repository
public interface ActivateLinkRepository extends JpaRepository<ActivateLink, Integer> {
    Optional<ActivateLink> findActivateLinkByLink(String link);
}
