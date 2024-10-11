package fit.iuh.backend.service;


import fit.iuh.backend.dto.OTPRequestDTO;
import fit.iuh.backend.dto.OTPResponseDTO;
import fit.iuh.backend.dto.PhoneNumberDTO;
import org.springframework.stereotype.Service;

@Service
public interface TwilioSMSService {
//    String sendSMSToVerify(PhoneNumberDTO phoneNumberDTO);
//    OTPResponseDTO verifyOTP(OTPRequestDTO otpRequestDTO);
    String sendSMSToVerifyV(PhoneNumberDTO phoneNumberDTO);
    OTPResponseDTO verifyOTPV(OTPRequestDTO otpRequestDTO);
}
