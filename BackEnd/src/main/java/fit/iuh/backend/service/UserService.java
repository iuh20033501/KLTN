package fit.iuh.backend.service;

import fit.iuh.backend.moudel.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findById(Long id);
    User createUser (User user);
    String getRole (Long id);


    List<User> findAll();

    Optional<User> findByHoTen(String name);

}
