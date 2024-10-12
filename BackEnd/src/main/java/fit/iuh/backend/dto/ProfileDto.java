package fit.iuh.backend.dto;

import fit.iuh.backend.moudel.TaiKhoanLogin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
 @AllArgsConstructor
public class ProfileDto {
    private TaiKhoanLogin tk;
    private String token;
    private String refreshToken;
}
