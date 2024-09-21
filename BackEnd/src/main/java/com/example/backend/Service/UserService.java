package com.example.backend.Service;

import com.example.backend.moudel.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findById(Long id);
    User createUser (User user);
    String getRole (Long id);

}
