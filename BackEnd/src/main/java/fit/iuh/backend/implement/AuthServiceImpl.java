package fit.iuh.backend.implement;

import fit.iuh.backend.dto.ProfileDto;
import fit.iuh.backend.dto.SignupDto;
import fit.iuh.backend.dto.TaiKhoanDto;
import fit.iuh.backend.enumclass.ChucVuEnum;
import fit.iuh.backend.jwt.JwtRequest;
import fit.iuh.backend.jwt.JwtResponse;
import fit.iuh.backend.jwt.RefreshTokenRequest;
import com.example.backend.moudel.*;
import fit.iuh.backend.moudel.*;
import fit.iuh.backend.repository.TaiKhoanRepo;
import com.example.backend.service.*;
import fit.iuh.backend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;

@Component
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private TaiKhoanRepo repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Qualifier("userImplement" )
    @Autowired
    private UserService service;
    @Qualifier("hocVienImplement")
    @Autowired
    private HocVienService hvService;
    @Qualifier("giangVienImplement")
    @Autowired
    private GiangVienService giangVienService;
    @Qualifier("nhanVienImplement")
    @Autowired
    private NhanVienService nhanVienService;
    @Override
    public ProfileDto signuphv(SignupDto dto) {
        Optional<TaiKhoanLogin> opUser = repository.findByTenDangNhap(dto.getUsername());
        if (opUser.isEmpty()) {

            HocVien p = new HocVien();
            p.setNgaySinh(dto.getBirthday());
            p.setGioiTinh(dto.isGender());
            p.setHoTen(dto.getName());
            p.setSdt(dto.getPhone());
            p.setEmail(dto.getEmail());
            p.setDiaChi(dto.getAddress());
            p.setKiNangCan(dto.getListKiNang());

            HocVien c = hvService.createHocVien(p);
            TaiKhoanLogin u = new TaiKhoanLogin(dto.getUsername(),passwordEncoder.encode(dto.getPassword()),true,c, ChucVuEnum.STUDENT);
            TaiKhoanLogin us = repository.save(u);
            JwtResponse r = signin(new JwtRequest(dto.getUsername(), dto.getPassword()));
            return new ProfileDto(us, r.getAccessToken(), r.getRefreshToken());
        } else {
            throw new RuntimeException("existedUser");
        }
    }

    @Override
    public ProfileDto signupgv(SignupDto dto) {
        Optional<TaiKhoanLogin> opUser = repository.findByTenDangNhap(dto.getUsername());
        if (opUser.isEmpty()) {

            GiangVien p = new GiangVien();
            p.setNgaySinh(dto.getBirthday());
            p.setGioiTinh(dto.isGender());
            p.setHoTen(dto.getName());
            p.setSdt(dto.getPhone());
            p.setEmail(dto.getEmail());
            p.setDiaChi(dto.getAddress());
            p.setChuyenMon(dto.getListKiNang());
            GiangVien c = giangVienService.createGiangVien(p);
            TaiKhoanLogin u = new TaiKhoanLogin(dto.getUsername(),passwordEncoder.encode(dto.getPassword()),true,c,ChucVuEnum.TEACHER);
            TaiKhoanLogin us = repository.save(u);
            JwtResponse r = signin(new JwtRequest(dto.getUsername(), dto.getPassword()));
            return new ProfileDto(us, r.getAccessToken(), r.getRefreshToken());
        } else {
            throw new RuntimeException("existedUser");
        }
    }

    @Override
    public ProfileDto signupadmin(SignupDto dto) {
        Optional<TaiKhoanLogin> opUser = repository.findByTenDangNhap(dto.getUsername());
        if (opUser.isEmpty()) {
            NhanVien p = new NhanVien();
            p.setNgaySinh(dto.getBirthday());
            p.setGioiTinh(dto.isGender());
            p.setHoTen(dto.getName());
            p.setSdt(dto.getPhone());
            p.setEmail(dto.getEmail());
            p.setDiaChi(dto.getAddress());
            p.setLuongThang(dto.getLuong());
            User c = nhanVienService.createNhanVien(p);
            TaiKhoanLogin u = new TaiKhoanLogin(dto.getUsername(),passwordEncoder.encode(dto.getPassword()),true,c,ChucVuEnum.ADMIN);
            TaiKhoanLogin us = repository.save(u);
            JwtResponse r = signin(new JwtRequest(dto.getUsername(), dto.getPassword()));
            return new ProfileDto(us, r.getAccessToken(), r.getRefreshToken());
        } else {
            throw new RuntimeException("existedUser");
        }
    }

    @Override
    public ProfileDto signupnv(SignupDto dto) {
        Optional<TaiKhoanLogin> opUser = repository.findByTenDangNhap(dto.getUsername());
        if (opUser.isEmpty()) {
            NhanVien p = new NhanVien();
            p.setNgaySinh(dto.getBirthday());
            p.setGioiTinh(dto.isGender());
            p.setHoTen(dto.getName());
            p.setSdt(dto.getPhone());
            p.setEmail(dto.getEmail());
            p.setDiaChi(dto.getAddress());
            p.setLuongThang(dto.getLuong());
            User c = nhanVienService.createNhanVien(p);
            TaiKhoanLogin u = new TaiKhoanLogin(dto.getUsername(),passwordEncoder.encode(dto.getPassword()),true,c,ChucVuEnum.QUANLY);
            TaiKhoanLogin us = repository.save(u);
            JwtResponse r = signin(new JwtRequest(dto.getUsername(), dto.getPassword()));
            return new ProfileDto(us, r.getAccessToken(), r.getRefreshToken());
        } else {
            throw new RuntimeException("existedUser");
        }
    }




    @Override
    public JwtResponse signin(JwtRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = repository.findByTenDangNhap(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("invalid username"));
        TaiKhoanDto dto = new TaiKhoanDto(user);
        var jwt = jwtService.generateToken(dto);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), dto);
        JwtResponse response = new JwtResponse();
        response.setAccessToken(jwt);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        String userName = jwtService.extractUserName(request.getToken());
        TaiKhoanLogin tk = repository.findByTenDangNhap(userName).orElseThrow(() -> new UsernameNotFoundException("user is not existed!"));
        TaiKhoanDto dto = new TaiKhoanDto(tk);
        if (jwtService.isTokenValid(request.getToken(), dto)) {
            var jwt = jwtService.generateToken(dto);
            JwtResponse response = new JwtResponse();
            response.setAccessToken(jwt);
            response.setRefreshToken(request.getToken());
            return response;
        }
        return null;
    }

    @Override
    public JwtResponse forgotPassword(String username) {
        TaiKhoanLogin tk = repository.findByTenDangNhap(username).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản " + username));
        if (tk.getEnable() == false) {
            throw new RuntimeException("Tài khoản " + username + " đã bị khóa.");
        }
        var tkdto = repository.findByTenDangNhap(username).orElseThrow(() -> new IllegalArgumentException("invalid username"));
        TaiKhoanDto dto = new TaiKhoanDto(tkdto);
        var jwt = jwtService.generateToken(dto);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), dto);
        JwtResponse response = new JwtResponse();
        response.setAccessToken(jwt);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @Override
    public String resetPassword(String token, String pass) {
        if (!jwtService.isTokenExprired(token)) {
            String username = jwtService.extractUserName(token);
            TaiKhoanLogin tk = repository.findByTenDangNhap(username).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user " + username));
            tk.setMatKhau(passwordEncoder.encode(pass));
            repository.save(tk);
            return "passUpdateSuccess";
        }
        throw new RuntimeException("passUpdateFailed");
    }

}
