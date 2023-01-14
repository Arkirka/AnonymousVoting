package ru.vorobyov.VotingServWithAuth.repositories;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ru.vorobyov.VotingServWithAuth.entities.RecoveryLink;
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
class RecoveryLinkRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RecoveryLinkRepository recoveryLinkRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void setUp() {
        recoveryLinkRepository.deleteAll();
    }

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(recoveryLinkRepository).isNotNull();
    }
    @Test
    void findRecoveryLinkByLink() {
    }

    @ParameterizedTest
    @MethodSource("shouldFindNoRecoveryLinkByLinkIfRepositoryIfEmptyCases")
    public void shouldFindNoRecoveryLinkByLinkIfRepositoryIfEmpty(String source) {
        RecoveryLink linkByLink = recoveryLinkRepository.findRecoveryLinkByLink(source);

        assertThat(linkByLink).isNull();
    }

    public Stream<Arguments> shouldFindNoRecoveryLinkByLinkIfRepositoryIfEmptyCases() {
        return Stream.of(
                Arguments.of("first"),
                Arguments.of("second"),
                Arguments.of("123213123"),
                Arguments.of("dsdasdasd213124efsdfs"),
                Arguments.of("sadsadsafty663")
        );
    }

    @ParameterizedTest
    @MethodSource("shouldFindRecoveryLinkByLinkIfRepositoryNotEmptyCases")
    public void shouldFindRecoveryLinkByLink(String source,
                                             RecoveryLink expected) {
        RecoveryLink plsWork = recoveryLinkRepository.findRecoveryLinkByLink(source);

        Assertions.assertEquals(expected, plsWork, "Entities not equals!");
    }

    public Stream<Arguments> shouldFindRecoveryLinkByLinkIfRepositoryNotEmptyCases() {
        String[] links = new String[] {
                "123123wqrrewr", "weqweqwesdf213", "123ewewfsd213efds", "hgfhfgh435436", "sdxcbcbfdh35436tretgdfg"
        };
        List<User> users = Util.createUsers(links.length, userRepository);
        List<RecoveryLink> recoveryLinks = IntStream.range(0, links.length)
                .unordered()
                .mapToObj(i -> {
                    RecoveryLink temp = new RecoveryLink();
                    temp.setUser(users.get(i));
                    temp.setLink(links[i]);
                    return temp;
                }).collect(Collectors.toList());
        recoveryLinkRepository.saveAll(recoveryLinks);
        recoveryLinks.clear();
        recoveryLinkRepository.findAll().forEach(recoveryLinks::add);
        return IntStream.range(0, links.length)
                .mapToObj(i ->
                        Arguments.of(recoveryLinks.get(i).getLink(), recoveryLinks.get(i))
                );
    }

    @Test
    void findRecoveryLinksByUser() {
    }

    @ParameterizedTest
    @MethodSource("shouldNotFindRecoveryLinkByUserIfEmptyCases")
    public void shouldNotFindRecoveryLinkByUserIfEmpty(User source) {
        RecoveryLink plsWork = recoveryLinkRepository.findRecoveryLinkByUser(source);
        assertThat(plsWork).isNull();
    }

    public Stream<Arguments> shouldNotFindRecoveryLinkByUserIfEmptyCases() {
        List<User> users = Util.createUsers(5, userRepository);
        return users.stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("shouldFindRecoveryLinkByUserCases")
    public void shouldFindRecoveryLinkByUser(User source,
                                             RecoveryLink expected) {
        RecoveryLink plsWork = recoveryLinkRepository.findRecoveryLinkByUser(source);

        Assertions.assertEquals(expected, plsWork, "Entities not equals!");
    }

    public Stream<Arguments> shouldFindRecoveryLinkByUserCases() {
        String[] links = new String[] {
                "123123wqrrewr", "weqweqwesdf213", "123ewewfsd213efds", "hgfhfgh435436", "sdxcbcbfdh35436tretgdfg"
        };
        List<User> users = Util.createUsers(links.length, userRepository);
        List<RecoveryLink> recoveryLinks = IntStream.range(0, links.length)
                .unordered()
                .mapToObj(i -> {
                    RecoveryLink temp = new RecoveryLink();
                    temp.setUser(users.get(i));
                    temp.setLink(links[i]);
                    return temp;
                }).collect(Collectors.toList());
        recoveryLinkRepository.saveAll(recoveryLinks);
        recoveryLinks.clear();
        recoveryLinkRepository.findAll().forEach(recoveryLinks::add);
        return IntStream.range(0, links.length)
                .mapToObj(i ->
                        Arguments.of(recoveryLinks.get(i).getUser(), recoveryLinks.get(i))
                );
    }


}