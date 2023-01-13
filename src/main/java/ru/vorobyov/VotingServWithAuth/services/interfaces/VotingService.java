package ru.vorobyov.VotingServWithAuth.services.interfaces;

import ru.vorobyov.VotingServWithAuth.entities.Voting;

import java.util.List;

public interface VotingService {
    List<Voting> findAll();
    void updateUser(int votingId, int yes, int no, int neutral, int broken) throws Exception;
    void save(Voting voting);
    void saveAll(Iterable<Voting> votingList);
    void deleteAll();
    void updateVotingSize(int userSize);
}
