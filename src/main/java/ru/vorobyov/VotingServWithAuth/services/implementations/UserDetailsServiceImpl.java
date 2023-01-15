package ru.vorobyov.VotingServWithAuth.services.implementations;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.vorobyov.VotingServWithAuth.entities.User;
import ru.vorobyov.VotingServWithAuth.repositories.UserRepository;
import ru.vorobyov.VotingServWithAuth.util.UserRoles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUserName(userName);

        user.orElseThrow(() -> new UsernameNotFoundException(userName + " not found."));

        return user.map(UserDetailsImpl::new).get();
    }

    public User findUserById(int userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public void changeActiveById(int userId, boolean isActive){
        User user = findUserById(userId);
        user.setActive(isActive);
        userRepository.save(user);
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean updateUser(User user) {
        Optional<User> userFromDB = userRepository.findById(user.getId());
        if (!userFromDB.isPresent())
            return false;
        user.setPassword(userFromDB.get().getPassword());
        userRepository.save(user);
        return true;
    }

    public boolean saveUser(User user) {
        Optional<User> userFromDB = userRepository.findByUserName(user.getUserName());
        if (userFromDB.isPresent())
            return false;
        user.setRoles(user.getRoles());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean saveUserDefaultUser(User user) {
        Optional<User> userFromDB = userRepository.findByUserName(user.getUserName());
        if (userFromDB.isPresent())
            return false;
        user.setUserName(user.getUserName().trim());
        user.setEmail(user.getEmail().trim());
        user.setFullName(user.getFullName().trim());
        user.setRoles(UserRoles.ROLE_USER.name());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword().trim()));

        userRepository.save(user);
        return true;
    }

    public void updateAllVotersToUsers(){
        List<User> userList = allUsers()
                .stream()
                .filter(el -> el.getRoles().equals(UserRoles.ROLE_VOTER.name()))
                .peek(el -> el.setRoles(UserRoles.ROLE_USER.name()))
                .collect(Collectors.toList());
        userRepository.saveAll(userList);
    }


    public boolean updateUserDefaultToVoter(User user) {
        Optional<User> userFromDB = userRepository.findByUserName(user.getUserName());
        if (!userFromDB.isPresent())
            return true;
        User temp = userFromDB.get();
        temp.setRoles(UserRoles.ROLE_VOTER.name());
        userRepository.save(temp);
        return false;
    }

    public boolean updateUserVoterToDefault(String username) {
        Optional<User> userFromDB = userRepository.findByUserName(username);
        if (!userFromDB.isPresent())
            return false;
        User user = userFromDB.get();
        user.setRoles(UserRoles.ROLE_USER.name());
        userRepository.save(user);
        return true;
    }

    public boolean updateUserPassword(User user){
        Optional<User> userFromDB = userRepository.findByUserName(user.getUserName());
        if (!userFromDB.isPresent())
            return false;
        User temp = userFromDB.get();
        temp.setPassword(new BCryptPasswordEncoder().encode(user.getPassword().trim()));
        userRepository.save(temp);
        return true;
    }

    public User findUserByUsername(String userName){
        Optional<User> userFromDB = userRepository.findByUserName(userName);
        return userFromDB.orElse(null);
    }

    public boolean isUserExist(User user){
        Optional<User> userOptionalFromDB = userRepository.findByUserName(user.getUserName());
        if (!userOptionalFromDB.isPresent())
            return false;
        User userFromDb = userOptionalFromDB.get();
        return userFromDb.getUserName().equals(user.getUserName())
                && userFromDb.getFullName().equals(user.getFullName())
                && userFromDb.getEmail().equals(user.getEmail());
    }

    public boolean isUserExist(int id){
        Optional<User> userOptionalFromDB = userRepository.findById(id);
        return userOptionalFromDB.isPresent();
    }

    public boolean saveUserDefaultAdmin(User user) {
        Optional<User> userFromDB = userRepository.findByUserName(user.getUserName());
        if (userFromDB.isPresent())
            return false;
        user.setRoles(UserRoles.ROLE_ADMIN.name());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(int userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
