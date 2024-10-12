package fit.iuh.backend.service;


import fit.iuh.backend.dto.ChangePassDTO;
import fit.iuh.backend.dto.ProfileDto;
import fit.iuh.backend.dto.SignupDto;

import fit.iuh.backend.jwt.JwtRequest;
import fit.iuh.backend.jwt.JwtResponse;
import fit.iuh.backend.jwt.RefreshTokenRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ProfileDto signupnv(SignupDto dto);
    ProfileDto signuphv(SignupDto dto);
    ProfileDto signupgv(SignupDto dto);
    ProfileDto signupadmin(SignupDto dto);
    JwtResponse signin(JwtRequest request);
    JwtResponse refreshToken(RefreshTokenRequest request);
    JwtResponse forgotPassword(String username);
    String resetPassword(Long id, String pass);
    String changePassword(Long id, ChangePassDTO dto);
}
