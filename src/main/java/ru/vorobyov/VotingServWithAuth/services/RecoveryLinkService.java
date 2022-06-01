package ru.vorobyov.VotingServWithAuth.services;

import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.List;
import java.util.Optional;

public interface RecoveryLinkService {
    public Optional<RecoveryLink> findRecoveryLinkByLink(String link);
    public Iterable<RecoveryLink> findAll();
    public void create(RecoveryLink recoveryLink);
    public List<Integer> findAllIdByUser(User user);
    public void deleteAll();
    public void deleteAllById(List<Integer> ids);
    public void delete(int id);
}
