package fit.iuh.backend.dto;

import lombok.*;

@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OTPResponseDTO {
    private String accessToken;
    private String refreshToken;
}