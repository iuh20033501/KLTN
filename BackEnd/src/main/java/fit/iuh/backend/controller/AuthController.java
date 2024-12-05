package fit.iuh.backend.controller;




import fit.iuh.backend.dto.*;
import fit.iuh.backend.jwt.JwtRequest;
import fit.iuh.backend.jwt.JwtResponse;
import fit.iuh.backend.jwt.RefreshTokenRequest;
import fit.iuh.backend.moudel.KhoaHoc;
import fit.iuh.backend.moudel.TaiKhoanLogin;
import fit.iuh.backend.moudel.User;
import fit.iuh.backend.service.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @Autowired
    private KhoaHocService khoaHocService;


    @Operation(
            summary = "Gửi thông tin tạo tài khoản",
            description = """ 
            thông tin chung : username(đặt điều kiện không là dạng sdt),name,password,address,image,coverImage,gender,phone(bắt buộc),birthday
            nếu là role là 1 tạo ra học viên có thêm List<enumkinang>
            nếu là role là 2 tạo ra giáo viên có thêm List<enumkinang> và lương
            Nếu là 3, 4 là  nhân viên và admin thêm lương
    """
    )

    @PostMapping("/account/signup/{role}")
//    @Operation(summary = "Đăng ký")
    public ResponseEntity<ProfileDto> signup(@RequestBody SignupDto dto, @PathVariable int role, @AuthenticationPrincipal TaiKhoanDto tkdto) {
        if(role ==1)
            return ResponseEntity.ok(service.signuphv(dto));
        else if(role ==2)
            return  ResponseEntity.ok(service.signupgv(dto));
        else if(role ==3)
            return  ResponseEntity.ok(service.signupnv(dto));
        else return  ResponseEntity.ok(service.signupadmin(dto));
    }


    @PostMapping("/noauth/signin")
    public ResponseEntity<?> signin(@RequestBody JwtRequest dto) {
        TaiKhoanLogin taiKhoan = tkService.findByTenDangNhap(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Tên đăng nhập không tồn tại: " + dto.getUsername()));

        if (!taiKhoan.getEnable()) {
            return ResponseEntity.badRequest()
                    .body("Tài khoản đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên để kích hoạt lại.");
        }

        try {
            JwtResponse jwtResponse = service.signin(dto);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Tên đăng nhập hoặc mật khẩu không đúng.");
        }
    }

    @PostMapping("/noauth/refresh")
//    @Operation(summary = "cập nhập lại token")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }
    @Autowired
    private TwilioSMSService twilioSMSService;

    @PostMapping("/noauth/send")
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
    @PostMapping("/noauth/validate")
    @Operation(
            summary = "Xác thực mã OTP",
            description = """
                    Mỗi làn chạy một làn xác thực bắt buộc phải chạy mọt hàm reset hoặc signup
                    
                    Gọi tới v1/auth/register để cập nhật lại các thông tin cơ bản
                    
                    Lưu ý: Số điện thoại phải là 10 và các đầu số phải thuộc các nhà mạng:
                    - Viettel (032, 033, 034, 035, 036, 037, 038, 039)
                    - Vinaphone (081, 082, 083, 084, 085, 088)
                    - MobiFone (070, 076, 077, 078, 079)
                    - Vietnamobile (052, 056, 058, 092)
                    - Gmobile (059, 099)
                    """)
    public OTPResponseDTO verifyOTP(@RequestBody OTPRequestDTO otpRequestDTO) {
        OTPResponseDTO response = twilioSMSService.verifyOTPV(otpRequestDTO);

        // Kiểm tra xem phản hồi có null hay không
        if (response.getAccessToken() == null && response.getRefreshToken() == null) {
            throw new IllegalArgumentException("OTP không chính xác hoặc đã hết hạn.");
        }

        return response;
    }

    public User authenProfile(TaiKhoanDto dto) {
        TaiKhoanLogin u = tkService.findByTenDangNhap(dto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Not found"));
        Optional<User> p = userService.findById(u.getUser().getIdUser());
        if (p.isPresent()) {
            return p.get();
        } else throw new RuntimeException("không tìm thấy profile user có hoc viên: " + u.getTenDangNhap());
    }
    @GetMapping("/profile")
    @Operation(summary = "Lấy thông tin user sau khi đăng nhập trả về thông tin user và enum chức vụ"
           )
    public SigninDTO validAdmin(@AuthenticationPrincipal TaiKhoanDto dto) {
        User u = authenProfile(dto);
        SigninDTO signinDTO= new SigninDTO(u,dto.getRole());
        return signinDTO;
    }
    @PostMapping("/account/reset")
    @Operation(
            summary = "Reset mật khẩu",
            description = """ 
            truyền resetDTO chứa password
    """
    )
    public ResponseEntity<String> resetPassword(@AuthenticationPrincipal TaiKhoanDto dto, @RequestBody ResetPassDto resetPassdto) {
        return ResponseEntity.ok(service.resetPassword( dto.getUsername(), resetPassdto.getPassword()));
    }
    @PostMapping("/account/changepass")
    @Operation(
            summary = "Đổi mật khẩu",
            description = """ 
            truyền changePassDTO chứa oldPass, newPass
    """
    )
    public ResponseEntity<String> resetPassword(@AuthenticationPrincipal TaiKhoanDto dto, @RequestBody ChangePassDTO changePassdto) {
        return ResponseEntity.ok(service.changePassword( dto.getId(), changePassdto));
    }
    @PostMapping("/account/changePassDestop")
    @Operation(
            summary = "Đổi mật khẩu ben destop",
            description = """ 
            truyền changePassDTO chứa oldPass, newPass
    """
    )
    public ResponseEntity<String> resetPasswordDestop(@AuthenticationPrincipal TaiKhoanDto dto, @RequestBody ChangePassDTO changePassdto) {
        String kQua = service.changePassword(dto.getId(), changePassdto);

        if (kQua.equals("passChangeSuccess")) {
            // Trả về phản hồi HTTP 200 OK nếu đổi mật khẩu thành công
            return ResponseEntity.ok(kQua);
        } else {
            // Trả về phản hồi HTTP 400 Bad Request nếu đổi mật khẩu thất bại
            return ResponseEntity.badRequest().body(kQua);
        }
    }

//    @PostMapping("/password/forgot")
//    @Operation(summary = "Quên mật khẩu")
//    public ResponseEntity<JwtResponse> forgotPassword(@RequestBody String tenDangNhap) {
//        return ResponseEntity.ok(service.forgotPassword(tenDangNhap));
//    }


    @GetMapping("/noauth/findAll")
    public List<TaiKhoanLogin> getAll(){
        return tkService.getAll();
    }
    @GetMapping("/noauth/findBySdt/{sdt}")
    public TaiKhoanLogin getBySDT(@PathVariable String sdt){
        return tkService.findBySDT(sdt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản với số điện thoại: " + sdt));

    }
    @GetMapping("/deleteById/{id}")
    public TaiKhoanLogin deleteById(@PathVariable Long id){
       TaiKhoanLogin tKhoan=  tkService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản với số id: " + id));
       tKhoan.setEnable(false);
        return tkService.createTaiKhoan(tKhoan);
    }
    @GetMapping("/findById/{id}")
    public TaiKhoanLogin findById(@PathVariable Long id){
        TaiKhoanLogin tKhoan=  tkService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản với số id: " + id));
        return tKhoan;
    }
    @GetMapping("/noauth/findByByUserName/{userName}")
    public TaiKhoanLogin findByUserName(@PathVariable String userName){
        TaiKhoanLogin tKhoan=  tkService.findByTenDangNhap(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản với số tên đăng nhập: " + userName));

        return tKhoan;
    }
    @Operation(
            summary = "láy all khoa học",
            description = """ 
           lấy all khóa học ko cần token
    """
    )
    @GetMapping("/noauth/findAllKhoa")
    public List<KhoaHoc> getAllKhoa(){
        return khoaHocService.getAll();
    }

    @GetMapping("/findTKhoan/{name}")
    public List<TaiKhoanLogin> getAllTKhoanLikeName(@PathVariable String name){
        return tkService.getListLikeName(name);
    }

    @GetMapping("/findTKhoanActiveLikeName/{name}")
    public List<TaiKhoanLogin> getAllTKhoanActiveTrue(@PathVariable String name){
        return tkService.getListTKActive(name);
    }
}