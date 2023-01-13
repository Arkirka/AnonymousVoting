package ru.vorobyov.VotingServWithAuth.services.interfaces;

import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;

import java.util.concurrent.CompletableFuture;

public interface ActivateLinkService {
    CompletableFuture<ActivateLink> findActivateLinkByLink(String link);
    CompletableFuture<Iterable<ActivateLink>> findAll();
    void create(ActivateLink activateLink);
    void delete(int id);
}