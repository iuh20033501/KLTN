package com.example.backend.implement;

import com.example.backend.Repository.UserRepo;
import com.example.backend.Service.UserService;
import com.example.backend.moudel.GiangVien;
import com.example.backend.moudel.HocVien;
import com.example.backend.moudel.NhanVien;
import com.example.backend.moudel.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

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
            return "null";
        }
    }
}
