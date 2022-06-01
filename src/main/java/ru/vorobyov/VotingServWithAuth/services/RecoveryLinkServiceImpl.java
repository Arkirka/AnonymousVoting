package ru.vorobyov.VotingServWithAuth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.repositories.RecoveryLinkRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("recoveryLinkService")
public class RecoveryLinkServiceImpl implements RecoveryLinkService{
    @Autowired
    private RecoveryLinkRepository recoveryLinkRepository;
    @Override
    public Optional<RecoveryLink> findRecoveryLinkByLink(String link) {
        return Optional.of(recoveryLinkRepository.findRecoveryLinkByLink(link));
    }

    @Override
    public Iterable<RecoveryLink> findAll() {
        return recoveryLinkRepository.findAll();
    }

    @Override
    public void create(RecoveryLink recoveryLink) {
        deleteAllById(findAllIdByUser(recoveryLink.getUser()));
        recoveryLinkRepository.save(recoveryLink);
    }

    @Override
    public List<Integer> findAllIdByUser(User user) {
        return recoveryLinkRepository.findRecoveryLinksByUser(user).stream().map(el -> el.getId()).collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        recoveryLinkRepository.deleteAll();
    }

    @Override
    public void deleteAllById(List<Integer> ids) {
        recoveryLinkRepository.deleteAllById(ids);
    }

    @Override
    public void delete(int id) {
        recoveryLinkRepository.deleteById(id);
    }
}
