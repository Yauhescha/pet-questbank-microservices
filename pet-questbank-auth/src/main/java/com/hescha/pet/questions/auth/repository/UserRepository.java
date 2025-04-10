package com.hescha.pet.questions.auth.repository;

import com.hescha.pet.questions.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
