package org.grisu.msvc.users.services;


import lombok.RequiredArgsConstructor;
import org.grisu.msvc.users.entities.Role;
import org.grisu.msvc.users.entities.User;
import org.grisu.msvc.users.repository.RoleRepository;
import org.grisu.msvc.users.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    
     @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.isAdmin()) {
            user.setRoles(roleRepository.findByName("ROLE_ADMIN").map(List::of).orElse(List.of()));
        }
            user.setRoles(roleRepository.findByName("ROLE_USER").map(List::of).orElse(List.of()));

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> updateUser(User user, Long id) {
        Optional<User> userOptional = this.getUserById(id);
        return userOptional.map(userDB -> {
            userDB.setEmail(user.getEmail());
            userDB.setUsername(user.getUsername());
            if (user.getEnabled() == null) {
                userDB.setEnabled(true);
            } else {
                userDB.setEnabled(user.getEnabled());
            }
            if (user.isAdmin()) {
                List<Role> existingRoles = userDB.getRoles() != null ? new ArrayList<>(userDB.getRoles()) : new ArrayList<>();
                roleRepository.findByName("ROLE_ADMIN").ifPresent(adminRole -> {
                    if (!existingRoles.contains(adminRole)) {
                        existingRoles.add(adminRole);
                    }
                });
                userDB.setRoles(existingRoles);
                userDB.setAdmin(true);
            }

            return userRepository.save(userDB);
        });
    }


    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
 
    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
