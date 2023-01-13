package ru.vorobyov.VotingServWithAuth.services.interfaces;

import ru.vorobyov.VotingServWithAuth.entities.Vote;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VoteService {
    CompletableFuture<Iterable<Vote>> findAll();
    void save(Vote vote);
    void saveAll(List<Vote> voteList);
    void deleteAll();
}
