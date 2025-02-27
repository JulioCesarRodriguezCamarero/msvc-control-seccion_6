package org.grisu.msvc.users.repository;

import org.grisu.libs.msvc.commons.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}

