package ru.vorobyov.VotingServWithAuth.repositories;

import org.junit.jupiter.params.provider.Arguments;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.util.UserRoles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Util {
    static List<User> createUsers(int length, UserRepository userRepository) {
        userRepository.deleteAll();
        List<User> users = IntStream.range(0, length)
                .mapToObj(i -> new User("name" + i, "email@email.com" + i, UserRoles.ROLE_USER))
                .collect(Collectors.toList());
        userRepository.saveAll(users);
        return userRepository.findAll();
    }

   static Stream<Arguments> oneArgumentStringCases() {
        return Stream.of(
                Arguments.arguments("first"),
                Arguments.arguments("second"),
                Arguments.arguments("123213123"),
                Arguments.arguments("dsdasdasd213124efsdfs"),
                Arguments.arguments("sadsadsafty663")
        );
    }
}
