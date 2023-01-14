package ru.vorobyov.VotingServWithAuth.services.interfaces;

import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.concurrent.CompletableFuture;

public interface RecoveryLinkService {
    CompletableFuture<RecoveryLink> findRecoveryLinkByLink(String link);
    CompletableFuture<Iterable<RecoveryLink>> findAll();
    void create(RecoveryLink recoveryLink);
    CompletableFuture<Integer> findIdByUser(User user);
    void deleteById(Integer id);
    void delete(int id);
}
