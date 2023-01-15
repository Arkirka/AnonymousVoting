package ru.vorobyov.VotingServWithAuth.services.implementations;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;
import ru.vorobyov.VotingServWithAuth.repositories.ActivateLinkRepository;
import ru.vorobyov.VotingServWithAuth.services.interfaces.ActivateLinkService;

import javax.persistence.EntityNotFoundException;
import java.util.concurrent.CompletableFuture;

@Service("activateLinkService")
public class ActivateLinkServiceImpl implements ActivateLinkService {
    public final ActivateLinkRepository activateLinkRepository;

    public ActivateLinkServiceImpl(ActivateLinkRepository activateLinkRepository) {
        this.activateLinkRepository = activateLinkRepository;
    }

    @Async
    @Override
    public CompletableFuture<ActivateLink> findActivateLinkByLink(String link) {
        StringBuilder exceptionMessage = new StringBuilder("Activate link ");
        return CompletableFuture.completedFuture(
                activateLinkRepository.findActivateLinkByLink(link).orElseThrow(() ->
                    new EntityNotFoundException(
                            exceptionMessage.append(link).append(" not found.").toString()
                    )
                )
        );
    }

    @Async
    @Override
    public CompletableFuture<Iterable<ActivateLink>> findAll() {
        return  CompletableFuture.completedFuture(activateLinkRepository.findAll());
    }

    @Async
    @Override
    public void create(ActivateLink activateLink) {
        activateLinkRepository.save(activateLink);
    }

    @Async
    @Override
    public void delete(int id) {
        activateLinkRepository.deleteById(id);
    }
}
