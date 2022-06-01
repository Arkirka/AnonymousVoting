package ru.vorobyov.VotingServWithAuth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.Vote;
import ru.vorobyov.VotingServWithAuth.repositories.VoteRepository;

import java.util.List;

@Service("voteService")
public class VoteServiceImpl implements VoteService{
    @Autowired
    public VoteRepository voteRepository;

    @Override
    public Iterable<Vote> findAll() {
        return voteRepository.findAll();
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
