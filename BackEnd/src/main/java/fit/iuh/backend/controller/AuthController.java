package fit.iuh.backend.controller;



import com.example.backend.dto.*;
import fit.iuh.backend.dto.*;
import fit.iuh.backend.jwt.JwtRequest;
import fit.iuh.backend.jwt.JwtResponse;
import fit.iuh.backend.jwt.RefreshTokenRequest;
import fit.iuh.backend.service.AuthService;
import fit.iuh.backend.service.TwilioSMSService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {


    @Qualifier("authServiceImpl")
    @Autowired
    private AuthService service;
//    @Autowired
//    private TaiKhoanService tkService;
//    @Autowired
//    private ProfileService profileService;

//    public Profile authenProfile(UserDto dto) {
//        User u = userService.findByUserName(dto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not found"));
//        Optional<Profile> p = profileService.findByUserId(u.getId());
//        if (p.isPresent()) {
//            return p.get();
//        } else throw new RuntimeException("không tìm thấy profile user có mssv: " + u.getMssv());
//    }

//    @GetMapping("/valid")
//    public String validAdmin(@AuthenticationPrincipal UserDto dto) {
//        if (dto != null) {
//            Profile p = authenProfile(dto);
//            if (dto.getRole().equals(ERole.ADMIN))
//                return "true";
//        }
//        return "false";
//    }

    @PostMapping("/signup/{role}")
//    @Operation(summary = "Đăng ký")
    public ResponseEntity<ProfileDto> signup(@RequestBody SignupDto dto, @PathVariable int role) {
        if(role ==1)
        return ResponseEntity.ok(service.signupnv(dto));
        else if(role ==2)
        return  ResponseEntity.ok(service.signupgv(dto));
        else if(role ==3)
            return  ResponseEntity.ok(service.signupnv(dto));
        else return  ResponseEntity.ok(service.signupadmin(dto));
    }

    @PostMapping("/signin")

    public ResponseEntity<JwtResponse> signin(@RequestBody JwtRequest dto) {
        return ResponseEntity.ok(service.signin(dto));
    }

    @PostMapping("/refresh")
//    @Operation(summary = "cập nhập lại token")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }
    @Autowired
    private TwilioSMSService twilioSMSService;

    @PostMapping("/send")
    @Operation(
            summary = "Gửi mã OTP tới số điện thoại. Dùng xác nhận số điện thoại để đăng ký tài khoản",
            description = """
                    Gửi mã OTP tới số điện thoại để đăng ký tài khoản. Thời hạn của OTP là 5 phút.
                    
                    Lưu ý: Số điện thoại phải là 10 và các đầu số phải thuộc các nhà mạng:
                    - Viettel (032, 033, 034, 035, 036, 037, 038, 039)
                    - Vinaphone (081, 082, 083, 084, 085, 088)
                    - MobiFone (070, 076, 077, 078, 079)
                    - Vietnamobile (052, 056, 058, 092)
                    - Gmobile (059, 099)
                    """
    )
    public String sendOTP( @RequestBody PhoneNumberDTO phoneNumberDTO) {
        return twilioSMSService.sendSMSToVerifyV(phoneNumberDTO);
    }

    @PostMapping("/validate")
    @Operation(
            summary = "Xác thực mã OTP",
            description = """
                    Nếu xác thực đúng thì trả về JWT và tạo ra tài khoản trong database với status là UNVERIFIED
                    
                    Gọi tới v1/auth/register để cập nhật lại các thông tin cơ bản
                    
                    Lưu ý: Số điện thoại phải là 10 và các đầu số phải thuộc các nhà mạng:
                    - Viettel (032, 033, 034, 035, 036, 037, 038, 039)
                    - Vinaphone (081, 082, 083, 084, 085, 088)
                    - MobiFone (070, 076, 077, 078, 079)
                    - Vietnamobile (052, 056, 058, 092)
                    - Gmobile (059, 099)
                    """)
    public OTPResponseDTO verifyOTP(@RequestBody OTPRequestDTO otpRequestDTO) {
        return twilioSMSService.verifyOTPV(otpRequestDTO);
    }

//    @PostMapping("/password/forgot")
//    @Operation(summary = "Quên mật khẩu")
//    public ResponseEntity<JwtResponse> forgotPassword(@RequestBody String tenDangNhap) {
//        return ResponseEntity.ok(service.forgotPassword(tenDangNhap));
//    }

    @PostMapping("/password/reset")
//    @Operation(summary = "Đổi mật khẩu")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassDto dto) {
        return ResponseEntity.ok(service.resetPassword(dto.getToken(), dto.getPassword()));
    }
}