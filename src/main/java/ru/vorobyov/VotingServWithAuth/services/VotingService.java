package ru.vorobyov.VotingServWithAuth.services;

import ru.vorobyov.VotingServWithAuth.entities.Voting;

import java.util.List;

public interface VotingService {
    public List<Voting> findAll();
    public void updateUser(int votingId, int yes, int no, int neutral, int broken);
    public void save(Voting voting);
    public void saveAll(Iterable<Voting> votingList);
    public void deleteAll();
    public void updateVotingSize(int userSize);
}
