package com.hescha.pet.questions.auth.service;

import com.hescha.pet.questions.auth.model.User;
import com.hescha.pet.questions.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService extends CrudService<User> implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public User update(Long id, User entity) {
        User read = read(id);
        if (read == null) {
            throw new RuntimeException("Entity User not found");
        }
        updateFields(entity, read);
        return update(read);

    }

    private void updateFields(User entity, User read) {
        read.setUsername(entity.getUsername());
        read.setPassword(passwordEncoder.encode(entity.getPassword()));
        read.setFirstName(entity.getFirstName());
        read.setLastName(entity.getLastName());
        read.setEmail(entity.getEmail());
        read.setPhone(entity.getPhone());
        read.setCurrentTokenId(entity.getCurrentTokenId());
    }

    public User register(String username, String password) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        return repository.save(user);
    }
}
