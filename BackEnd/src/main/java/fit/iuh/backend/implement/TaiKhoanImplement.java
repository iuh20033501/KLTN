package fit.iuh.backend.implement;


import fit.iuh.backend.dto.TaiKhoanDto;
import fit.iuh.backend.moudel.TaiKhoanLogin;
import fit.iuh.backend.repository.TaiKhoanRepo;
import fit.iuh.backend.repository.UserRepo;
import fit.iuh.backend.service.TaiKhoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public class TaiKhoanImplement implements TaiKhoanService {
    @Autowired
    private TaiKhoanRepo taiKhoanRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public Optional<TaiKhoanLogin> findByTenDangNhap(String tenDangNhap) {
        return taiKhoanRepo.findByTenDangNhap(tenDangNhap);
    }

    @Override
    public TaiKhoanLogin createTaiKhoan(TaiKhoanLogin taiKhoan) {
        return taiKhoanRepo.save(taiKhoan);
    }

    @Override
    public List<TaiKhoanLogin> getAll() {
        return taiKhoanRepo.findAll();
    }

    @Override
    public Optional<TaiKhoanLogin> findById(Long id) {
        return taiKhoanRepo.findById(id);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                TaiKhoanLogin u = taiKhoanRepo.findByTenDangNhap(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
                TaiKhoanDto dto = new TaiKhoanDto(u);
                return dto;
            }
        };
    }

    @Override
    public Optional<TaiKhoanLogin> findBySDT(String sdt) {
        return taiKhoanRepo.findSDT(sdt);

    }

}
