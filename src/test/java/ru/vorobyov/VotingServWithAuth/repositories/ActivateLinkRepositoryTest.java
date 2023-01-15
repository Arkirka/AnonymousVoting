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
import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;
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
class ActivateLinkRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivateLinkRepository activateLinkRepository;

    @AfterEach
    void tearDown() {
        activateLinkRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("shouldFindNoActivateLinkByLinkIfRepositoryIsEmptyCases")
    public void shouldFindNoActivateLinkByLinkIfRepositoryIsEmpty(String source) {
        Assertions.assertFalse(
                activateLinkRepository.findActivateLinkByLink(source).isPresent()
        );
    }

    public Stream<Arguments> shouldFindNoActivateLinkByLinkIfRepositoryIsEmptyCases() {
        return Util.oneArgumentStringCases();
    }

    @ParameterizedTest
    @MethodSource("shouldFindActivateLinkByLinkIfRepositoryNotEmptyCases")
    public void shouldFindActivateLinkByLink(String link, ActivateLink activateLink) {
        Optional<ActivateLink> linkByLink = activateLinkRepository.findActivateLinkByLink(link);

        Assertions.assertEquals(activateLink, linkByLink.orElse(null), "Entities not equals!");
    }

    public Stream<Arguments> shouldFindActivateLinkByLinkIfRepositoryNotEmptyCases() {
        String[] links = new String[] {
                "123123wqrrewr", "weqweqwesdf213", "123ewewfsd213efds", "hgfhfgh435436", "sdxcbcbfdh35436tretgdfg"
        };
        List<User> users = Util.createUsers(links.length, userRepository);
        List<ActivateLink> activateLinks = IntStream.range(0, links.length)
                .unordered()
                .mapToObj(i -> new ActivateLink(links[i], users.get(i)))
                .collect(Collectors.toList());
        activateLinkRepository.saveAll(activateLinks);
        activateLinks.clear();
        activateLinks.addAll(activateLinkRepository.findAll());
        return IntStream.range(0, links.length)
                .mapToObj(i -> Arguments.arguments(activateLinks.get(i).getLink(), activateLinks.get(i)));
    }


}