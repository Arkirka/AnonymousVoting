package ru.vorobyov.VotingServWithAuth.repositories;

import ru.vorobyov.VotingServWithAuth.entities.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {
    static List<User> createUsers(int length, UserRepository userRepository) {
        userRepository.deleteAll();
        List<User> users = IntStream.range(0, length).mapToObj(i -> new User()).collect(Collectors.toList());
        userRepository.saveAll(users);
        return userRepository.findAll();
    }
}
