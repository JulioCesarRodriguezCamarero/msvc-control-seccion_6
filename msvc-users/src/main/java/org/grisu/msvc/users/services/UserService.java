package org.grisu.msvc.users.services;

import org.grisu.msvc.users.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User saveUser(User user);
   Optional<User> updateUser(User user, Long id);
    void deleteUserById(Long id);
    User findByUsername(String username);

}
