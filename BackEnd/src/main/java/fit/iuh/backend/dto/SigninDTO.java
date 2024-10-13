package fit.iuh.backend.dto;

import fit.iuh.backend.enumclass.ChucVuEnum;
import fit.iuh.backend.moudel.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninDTO {
    private User u;
    private ChucVuEnum cvEnum;
}
