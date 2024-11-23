package fit.iuh.backend.moudel;

import fit.iuh.backend.dto.TaiKhoanDto;
import fit.iuh.backend.enumclass.ChucVu;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tenDangNhap;
    private String matKhau;
    private Boolean enable;
    @OneToOne
    private User user;
    @Enumerated(value = EnumType.STRING)
    private ChucVu role;
    public TaiKhoanLogin(TaiKhoanDto tk) {
        this.id = tk.getId();
        this.tenDangNhap = tk.getUsername();
        this.matKhau = tk.getPassword();
        this.enable = tk.getEnable();
        this.user = tk.getUser();
        this.role= tk.getRole();
    }
    public TaiKhoanLogin(String tenDangNhap, String matKhau, Boolean enable, User user, ChucVu role) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.enable = enable;
        this.user = user;
        this.role = role;
    }

}
