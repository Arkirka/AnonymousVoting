package ru.vorobyov.VotingServWithAuth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vorobyov.VotingServWithAuth.entities.Voting;

@Repository
public interface VotingDefaultRepository extends JpaRepository<Voting, Integer> {
}
