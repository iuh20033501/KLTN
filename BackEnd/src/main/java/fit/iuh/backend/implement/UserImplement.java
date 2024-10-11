package fit.iuh.backend.implement;

import fit.iuh.backend.repository.UserRepo;
import fit.iuh.backend.service.UserService;
import fit.iuh.backend.moudel.GiangVien;
import fit.iuh.backend.moudel.HocVien;
import fit.iuh.backend.moudel.NhanVien;
import fit.iuh.backend.moudel.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class UserImplement implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public User createUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public String getRole(Long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user instanceof GiangVien) {
            return "GiaoVien";
        } else if (user instanceof HocVien) {
            return "HocVien";
        }else if (user instanceof NhanVien) {
            return "NhanVien";
        } else {
            return "ADMIN";
        }
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Optional<User> findByHoTen(String name) {
        return userRepo.findByHoTen(name);
    }
}
