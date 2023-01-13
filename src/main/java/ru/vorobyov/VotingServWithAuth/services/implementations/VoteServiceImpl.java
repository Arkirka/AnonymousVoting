package ru.vorobyov.VotingServWithAuth.services.implementations;

import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.Vote;
import ru.vorobyov.VotingServWithAuth.repositories.VoteRepository;
import ru.vorobyov.VotingServWithAuth.services.interfaces.VoteService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service("voteService")
public class VoteServiceImpl implements VoteService {
    public final VoteRepository voteRepository;

    public VoteServiceImpl(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public CompletableFuture<Iterable<Vote>> findAll() {
        return CompletableFuture.completedFuture(voteRepository.findAll());
    }

    @Override
    public void save(Vote vote) {
        voteRepository.save(vote);
    }

    @Override
    public void saveAll(List<Vote> voteList) {
        voteRepository.saveAll(voteList);
    }

    @Override
    public void deleteAll() {
        voteRepository.deleteAll();
    }
}
