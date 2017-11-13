package com.depromeet.todo.service;

import com.depromeet.todo.dto.page.UserForm;
import com.depromeet.todo.model.User;
import com.depromeet.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User register(UserForm form) {

        User user = new User();

        user.setAccount(form.getUsername());
        user.setHash(encoder.encode(form.getPassword()));

        User created = userRepository.save(user);
        created.setHash(null);

        return created;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User found = userRepository.findByAccount(username);

        if (found == null) {
            throw new UsernameNotFoundException("");
        }

        return found;
    }
}
