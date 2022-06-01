package ru.vorobyov.VotingServWithAuth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.Voting;
import ru.vorobyov.VotingServWithAuth.repositories.VotingDefaultRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("votingDefaultService")
public class VotingServiceImpl implements VotingService {
    @Autowired
    public VotingDefaultRepository votingDefaultRepository;
    @Override
    public List<Voting> findAll() {
        return votingDefaultRepository.findAll();
    }

    @Override
    public void updateUser(int votingId, int yes, int no, int neutral, int broken) {
        Optional<Voting> votingFromDb = votingDefaultRepository.findById(votingId);
        Voting voting = votingFromDb.get();
        voting.setYes(voting.getYes() + yes);
        voting.setNo(voting.getNo() + no);
        voting.setNeutral(voting.getNeutral() + neutral);
        voting.setBroken(voting.getBroken() + broken);
        votingDefaultRepository.save(voting);
    }

    @Override
    public void save(Voting voting) {
        votingDefaultRepository.save(voting);
    }

    @Override
    public void saveAll(Iterable<Voting> votingList) {
        votingDefaultRepository.saveAll(votingList);
    }

    @Override
    public void deleteAll() {
        votingDefaultRepository.deleteAll();
    }
    @Override
    public void updateVotingSize(int userSize){
        List<Voting> votingList = findAll().stream().peek(el -> el.setUserSize(userSize)).collect(Collectors.toList());
        saveAll(votingList);
    }
}
