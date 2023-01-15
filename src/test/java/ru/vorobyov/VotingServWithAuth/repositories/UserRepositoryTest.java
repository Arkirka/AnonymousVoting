package ru.vorobyov.VotingServWithAuth.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

@TestPropertySource("classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @MethodSource("shouldFindNoUserByUserNameIfRepositoryEmptyCases")
    public void shouldFindNoUserByUserNameIfRepositoryEmpty(String source) {
        Optional<User> userByUserName = userRepository.findByUserName(source);

        assertFalse(userByUserName.isPresent());
    }

    public Stream<Arguments> shouldFindNoUserByUserNameIfRepositoryEmptyCases() {
        return Util.oneArgumentStringCases();
    }

    @ParameterizedTest
    @MethodSource("shouldFindUserByUserNameCases")
    public void shouldFindUserByUserName(String username,
                                             User expected) {
        Optional<User> plsWork = userRepository.findByUserName(username);
        if (!plsWork.isPresent())
            Assertions.fail("Entity is null!");
        Assertions.assertEquals(expected, plsWork.get(), "Entities not equals!");
    }

    public Stream<Arguments> shouldFindUserByUserNameCases() {
        List<User> users = Util.createUsers(10, userRepository);
        return users.stream().map(user -> Arguments.of(user.getUserName(), user));
    }
}