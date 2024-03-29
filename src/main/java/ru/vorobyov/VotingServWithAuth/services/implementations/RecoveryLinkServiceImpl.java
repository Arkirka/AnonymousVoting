package ru.vorobyov.VotingServWithAuth.services.implementations;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.repositories.RecoveryLinkRepository;
import ru.vorobyov.VotingServWithAuth.services.interfaces.RecoveryLinkService;

import javax.persistence.EntityNotFoundException;
import java.util.concurrent.CompletableFuture;

@Service("recoveryLinkService")
public class RecoveryLinkServiceImpl implements RecoveryLinkService {
    private final RecoveryLinkRepository recoveryLinkRepository;

    public RecoveryLinkServiceImpl(RecoveryLinkRepository recoveryLinkRepository) {
        this.recoveryLinkRepository = recoveryLinkRepository;
    }

    @Async
    @Override
    public CompletableFuture<RecoveryLink> findRecoveryLinkByLink(String link) {
        StringBuilder exceptionMessage = new StringBuilder("Recovery link ");
        return CompletableFuture.completedFuture(
                recoveryLinkRepository.findRecoveryLinkByLink(link).orElseThrow(() ->
                    new EntityNotFoundException(
                            exceptionMessage.append(link).append(" not found.").toString()
                    )
                )
        );
    }

    @Async
    @Override
    public CompletableFuture<Iterable<RecoveryLink>> findAll() {
        return CompletableFuture.completedFuture(recoveryLinkRepository.findAll());
    }

    @Async
    @Override
    public void create(RecoveryLink recoveryLink) {
        findIdByUser(recoveryLink.getUser()).thenAcceptAsync(result -> {
            deleteById(result);
            recoveryLinkRepository.save(recoveryLink);
        });
    }

    @Async
    @Override
    public CompletableFuture<Integer> findIdByUser(User user) {
        StringBuilder exceptionMessage = new StringBuilder("Recovery link ");
        return CompletableFuture.completedFuture(
            recoveryLinkRepository.findRecoveryLinkByUser(user)
                    .orElseThrow(() -> new EntityNotFoundException(
                            exceptionMessage.append(user.getUserName()).append(" not found.").toString()
                    ))
                    .getId()
        );
    }

    @Override
    public void deleteById(Integer id) {
        recoveryLinkRepository.deleteById(id);
    }

    @Async
    @Override
    public void delete(int id) {
        recoveryLinkRepository.deleteById(id);
    }
}
