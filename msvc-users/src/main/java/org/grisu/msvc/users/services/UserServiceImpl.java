package org.grisu.msvc.users.services;

import lombok.RequiredArgsConstructor;
import org.grisu.libs.msvc.commons.entities.Role;
import org.grisu.libs.msvc.commons.entities.User;
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

    private List<Role> checkAndSetRoles(User user) {
        List<Role> roles = new ArrayList<>(Optional.ofNullable(user.getRoles()).orElseGet(ArrayList::new));

        if (user.isAdmin() || roles.stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()))) {
            roleRepository.findByName("ROLE_ADMIN").ifPresent(adminRole -> {
                if (!roles.contains(adminRole)) {
                    roles.add(adminRole);
                }
            });
            user.setAdmin(true);
        } else {
            user.setAdmin(false);
        }

        roleRepository.findByName("ROLE_USER").ifPresent(userRole -> {
            if (!roles.contains(userRole)) {
                roles.add(userRole);
            }
        });

        user.setRoles(roles.stream().distinct().toList());
        return roles;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        users.forEach(user -> {
            List<Role> userRoles = checkAndSetRoles(user);
            user.setRoles(userRoles);
        });
        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(this::checkAndSetRoles);
        return user;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        checkAndSetRoles(user);
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
            checkAndSetRoles(userDB);
            return userRepository.save(userDB);
        });
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        Optional<User> user = this.getUserById(id);
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        checkAndSetRoles(user);
        return user;
    }
}
