package ru.vorobyov.VotingServWithAuth.services.interfaces;

import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RecoveryLinkService {
    CompletableFuture<RecoveryLink> findRecoveryLinkByLink(String link);
    CompletableFuture<Iterable<RecoveryLink>> findAll();
    void create(RecoveryLink recoveryLink);
    CompletableFuture<List<Integer>> findAllIdByUser(User user);
    void deleteAllById(List<Integer> ids);
    void delete(int id);
}
