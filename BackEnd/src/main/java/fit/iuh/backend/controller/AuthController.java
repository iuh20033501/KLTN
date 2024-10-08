package fit.iuh.backend.controller;




import fit.iuh.backend.dto.*;
import fit.iuh.backend.enumclass.ChucVuEnum;
import fit.iuh.backend.jwt.JwtRequest;
import fit.iuh.backend.jwt.JwtResponse;
import fit.iuh.backend.jwt.RefreshTokenRequest;
import fit.iuh.backend.moudel.TaiKhoanLogin;
import fit.iuh.backend.moudel.User;
import fit.iuh.backend.service.AuthService;
import fit.iuh.backend.service.TaiKhoanService;
import fit.iuh.backend.service.TwilioSMSService;
import fit.iuh.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {


    @Qualifier("authServiceImpl")
    @Autowired
    private AuthService service;
    @Qualifier("taiKhoanImplement")
    @Autowired
    private TaiKhoanService tkService;
    @Autowired
    private UserService userService;

    public User authenProfile(TaiKhoanDto dto) {
        TaiKhoanLogin u = tkService.findByTenDangNhap(dto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not found"));
       Optional<User> p = userService.findById(u.getUser().getIdUser());
        if (p.isPresent()) {
            return p.get();
        } else throw new RuntimeException("không tìm thấy profile user có hoc viên: " + u.getTenDangNhap());
    }

    @GetMapping("/profile")
    @Operation(summary = "Lấy thông tin user sau khi đăng nhập trả về thông tin user và enum chức vụ")
    public SigninDTO validAdmin(@AuthenticationPrincipal TaiKhoanDto dto) {
            User u = authenProfile(dto);
            SigninDTO signinDTO= new SigninDTO(u,dto.getRole());
            return signinDTO;
        }

    @Operation(
            summary = "Gửi thông tin tạo tài khoản",
            description = """ 
            thông tin chung : username,name,password,address,image,coverImage,gender,phone(bắt buộc),birthday
            nếu là role là 1 tạo ra học viên có thêm List<enumkinang>
            nếu là role là 2 tạo ra giáo viên có thêm List<enumkinang> và lương
            Nếu là 3, 4 là  nhân viên và admin thêm lương
    """
    )

    @PostMapping("/signup/{role}")
//    @Operation(summary = "Đăng ký")
    public ResponseEntity<ProfileDto> signup(@RequestBody SignupDto dto, @PathVariable int role) {
        if(role ==1)
        return ResponseEntity.ok(service.signuphv(dto));
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

    @PostMapping("/signup/reset")
   @Operation(
           summary = "Đổi mật khẩu",
           description = """ 
            gửi tokem và New pass
    """
   )
    public ResponseEntity<String> resetPassword(@AuthenticationPrincipal TaiKhoanDto dto, @RequestBody String Newpass) {
        return ResponseEntity.ok(service.resetPassword( dto.getId(), Newpass));
    }
    @GetMapping("/findAll")
    public List<TaiKhoanLogin> getAll(){
        return tkService.getAll();
    }
    @GetMapping("findBySdt/{sdt}")
    public TaiKhoanLogin getBySDT(@PathVariable String sdt){
        return tkService.findBySDT(sdt).get();
    }
}