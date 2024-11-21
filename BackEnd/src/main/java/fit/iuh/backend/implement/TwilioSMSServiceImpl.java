package fit.iuh.backend.implement;

import fit.iuh.backend.config.TwilioProperties;
import fit.iuh.backend.dto.OTPRequestDTO;
import fit.iuh.backend.dto.OTPResponseDTO;
import fit.iuh.backend.dto.PhoneNumberDTO;
import fit.iuh.backend.dto.TaiKhoanDto;
import fit.iuh.backend.enumclass.ChucVuEnum;
import fit.iuh.backend.moudel.TaiKhoanLogin;
import fit.iuh.backend.repository.TaiKhoanRepo;
import fit.iuh.backend.service.JwtService;
import fit.iuh.backend.service.TaiKhoanService;
import fit.iuh.backend.service.TwilioSMSService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.DecimalFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TwilioSMSServiceImpl implements TwilioSMSService {

    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TaiKhoanRepo tkRepo;


    @Autowired
    private TwilioProperties twilioConfig;
    Map<String, String> otpMap = new HashMap<>();
    public TwilioSMSServiceImpl(TwilioProperties twilioConfig) {
        this.twilioConfig = twilioConfig;
        Twilio.init(twilioConfig.getAccountSID(), twilioConfig.getAuthToken()); // Initialize Twilio
    }
    @Override
    public String sendSMSToVerifyV(PhoneNumberDTO phoneNumberDTO) {

        try {
            PhoneNumber to = new PhoneNumber(chinhSoPhone(phoneNumberDTO.getPhone()));
            PhoneNumber from = new PhoneNumber(twilioConfig.getPhoneNumberTrial()); // from
            String otp = generateOTP();
            String otpMessage = otp;
            Message message = Message
                    .creator(to, from,
                            otpMessage)
                    .create();
            otpMap.put(phoneNumberDTO.getPhone(), otp);
            return otp;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @Override
    public OTPResponseDTO verifyOTPV(OTPRequestDTO otpRequestDTO) {
        String storedOtp = otpMap.get(otpRequestDTO.getPhone());

        // Kiểm tra xem OTP nhập vào có khác với OTP đã lưu không
        if (storedOtp == null || !otpRequestDTO.getOtp().equals(storedOtp)) {
            return new OTPResponseDTO(null, null);
        }

        // Nếu OTP đúng, xóa OTP đã lưu
        otpMap.remove(otpRequestDTO.getPhone());

        // Tiến hành tìm kiếm tài khoản
        Optional<TaiKhoanLogin> tkOptional = taiKhoanService.findBySDT(otpRequestDTO.getPhone());
        TaiKhoanLogin tk = new TaiKhoanLogin();
        tk.setRole(ChucVuEnum.STUDENT);
        tk.setTenDangNhap(otpRequestDTO.getPhone());
        TaiKhoanLogin tk0 = tkRepo.save(tk);

        TaiKhoanDto dto = new TaiKhoanDto(tk0);
        var jwt = jwtService.generateToken(dto);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), dto);

        return new OTPResponseDTO(jwt, refreshToken);
    }

    private String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
    public String chinhSoPhone(String phone) {
        if (phone.startsWith("0")) {
            phone = "+84" + phone.substring(1);
            System.out.println(phone);
        }
        return phone;
    }


}
