//package com.example.backend.implement;
//
//import fit.se.jwt.dto.UserDto;
//import fit.se.jwt.enums.ERole;
//import fit.se.jwt.enums.UserStatus;
//import fit.se.jwt.models.User;
//import fit.se.jwt.repositories.UserRepository;
//import fit.se.jwt.services.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class serServiceImpl implements UserService {
//    @Autowired
//    private UserRepository repository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public UserServiceImpl(UserRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public User save(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return repository.save(user);
//    }
//
//    @Override
//    public Optional<User> findById(Long id) {
//        return repository.findById(id);
//    }
//
//    @Override
//    public Optional<User> findByUserName(String username) {
//        return repository.findByUsername(username);
//    }
//
////    @Override
////    public Optional<User> findByEmail(String email) {
////        return repository.findByEmail(email);
////    }
//
//    @Override
//    public List<User> findAll() {
//        return repository.findAll();
//    }
//
//    @Override
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                User u = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
//                UserDto dto = new UserDto(u);
//                return dto;
//            }
//        };
//    }
//
//    @Override
//    public List<User> findByStatus(UserStatus status) {
//        return repository.findByStatus(status);
//    }
//
//    @Override
//    public List<User> findByRole( ERole role) {
//        Sort sort = Sort.by(Sort.Direction.fromString("desc"), "updateAt");
//        return repository.findByRole(role, sort);
//    }
//
////    @Override
////    public List<User> findByCreateAt(LocalDateTime createAt) {
////        return repository.findByCreateAt(createAt);
////    }
//}
