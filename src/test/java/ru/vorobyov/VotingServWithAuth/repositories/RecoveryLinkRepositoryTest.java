package ru.vorobyov.VotingServWithAuth.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class RecoveryLinkRepositoryTest {

    @Autowired
    private RecoveryLinkRepository recoveryLinkRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void setUp() {
        recoveryLinkRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("shouldFindNoRecoveryLinkByLinkIfRepositoryIfEmptyCases")
    public void shouldFindNoRecoveryLinkByLinkIfRepositoryIfEmpty(String source) {
        Assertions.assertFalse(
                recoveryLinkRepository.findRecoveryLinkByLink(source).isPresent()
        );
    }

    public Stream<Arguments> shouldFindNoRecoveryLinkByLinkIfRepositoryIfEmptyCases() {
        return Util.oneArgumentStringCases();
    }

    @ParameterizedTest
    @MethodSource("shouldFindRecoveryLinkByLinkIfRepositoryNotEmptyCases")
    public void shouldFindRecoveryLinkByLink(String source,
                                             RecoveryLink expected) {
        Optional<RecoveryLink> plsWork = recoveryLinkRepository.findRecoveryLinkByLink(source);

        Assertions.assertEquals(expected, plsWork.orElse(null), "Entities not equals!");
    }

    public Stream<Arguments> shouldFindRecoveryLinkByLinkIfRepositoryNotEmptyCases() {
        String[] links = new String[] {
                "123123wqrrewr", "weqweqwesdf213", "123ewewfsd213efds", "hgfhfgh435436", "sdxcbcbfdh35436tretgdfg"
        };
        List<User> users = Util.createUsers(links.length, userRepository);
        List<RecoveryLink> recoveryLinks = IntStream.range(0, links.length)
                .unordered()
                .mapToObj(i -> new RecoveryLink(links[i], users.get(i)))
                .collect(Collectors.toList());
        recoveryLinkRepository.saveAll(recoveryLinks);
        recoveryLinks.clear();
        recoveryLinks.addAll(recoveryLinkRepository.findAll());
        return IntStream.range(0, links.length)
                .mapToObj(i -> Arguments.of(recoveryLinks.get(i).getLink(), recoveryLinks.get(i)));
    }

    @ParameterizedTest
    @MethodSource("shouldNotFindRecoveryLinkByUserIfEmptyCases")
    public void shouldNotFindRecoveryLinkByUserIfEmpty(User source) {
        Assertions.assertFalse(
                recoveryLinkRepository.findRecoveryLinkByUser(source).isPresent()
        );
    }

    public Stream<Arguments> shouldNotFindRecoveryLinkByUserIfEmptyCases() {
        List<User> users = Util.createUsers(5, userRepository);
        return users.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("shouldFindRecoveryLinkByUserCases")
    public void shouldFindRecoveryLinkByUser(User source,
                                             RecoveryLink expected) {
        Optional<RecoveryLink> plsWork = recoveryLinkRepository.findRecoveryLinkByUser(source);

        Assertions.assertEquals(expected, plsWork.orElse(null), "Entities not equals!");
    }

    public Stream<Arguments> shouldFindRecoveryLinkByUserCases() {
        String[] links = new String[] {
                "123123wqrrewr", "weqweqwesdf213", "123ewewfsd213efds", "hgfhfgh435436", "sdxcbcbfdh35436tretgdfg"
        };
        List<User> users = Util.createUsers(links.length, userRepository);
        List<RecoveryLink> recoveryLinks = IntStream.range(0, links.length)
                .unordered()
                .mapToObj(i -> new RecoveryLink(links[i], users.get(i)))
                .collect(Collectors.toList());
        recoveryLinkRepository.saveAll(recoveryLinks);
        recoveryLinks.clear();
        recoveryLinks.addAll(recoveryLinkRepository.findAll());
        return IntStream.range(0, links.length)
                .mapToObj(i -> Arguments.of(recoveryLinks.get(i).getUser(), recoveryLinks.get(i)));
    }
}