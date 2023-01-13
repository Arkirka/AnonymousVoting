package ru.vorobyov.VotingServWithAuth.services.implementations;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.repositories.RecoveryLinkRepository;
import ru.vorobyov.VotingServWithAuth.services.interfaces.RecoveryLinkService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service("recoveryLinkService")
public class RecoveryLinkServiceImpl implements RecoveryLinkService {
    private final RecoveryLinkRepository recoveryLinkRepository;

    public RecoveryLinkServiceImpl(RecoveryLinkRepository recoveryLinkRepository) {
        this.recoveryLinkRepository = recoveryLinkRepository;
    }

    @Async
    @Override
    public CompletableFuture<RecoveryLink> findRecoveryLinkByLink(String link) {
        return CompletableFuture.completedFuture(recoveryLinkRepository.findRecoveryLinkByLink(link));
    }

    @Async
    @Override
    public CompletableFuture<Iterable<RecoveryLink>> findAll() {
        return CompletableFuture.completedFuture(recoveryLinkRepository.findAll());
    }

    @Async
    @Override
    public void create(RecoveryLink recoveryLink) {
        findAllIdByUser(recoveryLink.getUser()).thenAcceptAsync(result -> {
            deleteAllById(result);
            recoveryLinkRepository.save(recoveryLink);
        });
    }

    @Async
    @Override
    public CompletableFuture<List<Integer>> findAllIdByUser(User user) {
        return CompletableFuture
                .completedFuture(
                        recoveryLinkRepository.findRecoveryLinksByUser(user)
                                .stream()
                                .map(RecoveryLink::getId)
                                .collect(Collectors.toList())
                );
    }

    @Override
    public void deleteAllById(List<Integer> ids) {
        recoveryLinkRepository.deleteAllById(ids);
    }

    @Async
    @Override
    public void delete(int id) {
        recoveryLinkRepository.deleteById(id);
    }
}
