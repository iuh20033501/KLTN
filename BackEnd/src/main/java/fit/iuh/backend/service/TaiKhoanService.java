package fit.iuh.backend.service;


import fit.iuh.backend.moudel.TaiKhoanLogin;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TaiKhoanService {
    Optional<TaiKhoanLogin> findByTenDangNhap(String tenDangNhap);
    TaiKhoanLogin createTaiKhoan(TaiKhoanLogin taiKhoan);
    List<TaiKhoanLogin> getAll();

    Optional<TaiKhoanLogin> findById(Long id);

    //    Optional<User> findByEmail(String email);

    UserDetailsService userDetailsService();
    Optional<TaiKhoanLogin> findBySDT (String sdt);
    List<TaiKhoanLogin> getListLikeName(String name);
    List<TaiKhoanLogin> getListTKActive();

}
