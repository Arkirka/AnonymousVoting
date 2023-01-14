package ru.vorobyov.VotingServWithAuth.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.vorobyov.VotingServWithAuth.entities.ActivateLink;
import ru.vorobyov.VotingServWithAuth.entities.User;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class ActivateLinkRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivateLinkRepository activateLinkRepository;

    @AfterEach
    void tearDown() {
        activateLinkRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(activateLinkRepository).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("shouldFindNoActivateLinkByLinkIfRepositoryIsEmptyCases")
    public void shouldFindNoActivateLinkByLinkIfRepositoryIsEmpty(String link) {
        ActivateLink linkByLink = activateLinkRepository.findActivateLinkByLink(link);

        assertThat(linkByLink).isNull();
    }

    public Stream<Arguments> shouldFindNoActivateLinkByLinkIfRepositoryIsEmptyCases() {
        return Stream.of(
                Arguments.arguments("first"),
                Arguments.arguments("second"),
                Arguments.arguments("123213123"),
                Arguments.arguments("dsdasdasd213124efsdfs"),
                Arguments.arguments("sadsadsafty663")
        );
    }

    @ParameterizedTest
    @MethodSource("shouldFindActivateLinkByLinkIfRepositoryNotEmptyCases")
    public void shouldFindActivateLinkByLink(String link,
                                             ActivateLink activateLink) {
        ActivateLink linkByLink = activateLinkRepository.findActivateLinkByLink(link);

        Assertions.assertEquals(activateLink, linkByLink, "Entities not equals!");
    }

    public Stream<Arguments> shouldFindActivateLinkByLinkIfRepositoryNotEmptyCases() {
        String[] links = new String[] {
                "123123wqrrewr", "weqweqwesdf213", "123ewewfsd213efds", "hgfhfgh435436", "sdxcbcbfdh35436tretgdfg"
        };
        List<User> users = Util.createUsers(links.length, userRepository);
        List<ActivateLink> activateLinks = IntStream.range(0, links.length)
                .unordered()
                .mapToObj(i -> {
                    ActivateLink temp = new ActivateLink();
                    temp.setUser(users.get(i));
                    temp.setLink(links[i]);
                    return temp;
                }).collect(Collectors.toList());
        activateLinkRepository.saveAll(activateLinks);
        activateLinks.clear();
        activateLinkRepository.findAll().forEach(activateLinks::add);
        return IntStream.range(0, links.length)
                .mapToObj(i ->
                        Arguments.arguments(activateLinks.get(i).getLink(), activateLinks.get(i))
                );
    }


}