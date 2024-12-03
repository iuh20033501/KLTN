package fit.iuh.backend.dto;

import fit.iuh.backend.moudel.NhanVien;
import fit.iuh.backend.moudel.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private NhanVien nhanVien;
    private String gson;

}
