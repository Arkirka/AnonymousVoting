package ru.vorobyov.VotingServWithAuth.services;

import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;

import java.util.Optional;

public interface ActivateLinkService {
    public Optional<ActivateLink> findActivateLinkByLink(String link);
    public Iterable<ActivateLink> findAll();
    public void create(ActivateLink activateLink);
    public void deleteAll();
    public void delete(int id);
}