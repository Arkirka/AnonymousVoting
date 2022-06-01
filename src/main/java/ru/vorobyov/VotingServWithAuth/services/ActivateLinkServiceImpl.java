package ru.vorobyov.VotingServWithAuth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;
import ru.vorobyov.VotingServWithAuth.repositories.ActivateLinkRepository;

import java.util.Optional;

@Service("activateLinkService")
public class ActivateLinkServiceImpl implements ActivateLinkService {
    @Autowired
    public ActivateLinkRepository activateLinkRepository;

    @Override
    public Optional<ActivateLink> findActivateLinkByLink(String link) {
        return Optional.of(activateLinkRepository.findActivateLinkByLink(link));
    }

    @Override
    public Iterable<ActivateLink> findAll() {
        return activateLinkRepository.findAll();
    }

    @Override
    public void create(ActivateLink activateLink) {
        activateLinkRepository.save(activateLink);
    }

    @Override
    public void deleteAll() {
        activateLinkRepository.deleteAll();
    }

    @Override
    public void delete(int id) {
        activateLinkRepository.deleteById(id);
    }
}
