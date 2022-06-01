package ru.vorobyov.VotingServWithAuth.services;

import ru.vorobyov.VotingServWithAuth.entities.Vote;

import java.util.List;

public interface VoteService {
    public Iterable<Vote> findAll();
    public void save(Vote vote);
    public void saveAll(List<Vote> voteList);
    public void deleteAll();
}
