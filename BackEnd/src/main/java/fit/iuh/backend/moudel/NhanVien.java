package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "nhan_vien")
public class NhanVien extends User{
    private Long luongThang;

    public NhanVien(Long idUser, String hoTen, String sdt, String diaChi, String email, LocalDate ngaySinh, boolean gioiTinh, String image) {
        super(idUser, hoTen, sdt, diaChi, email, ngaySinh, gioiTinh, image);
    }

    public NhanVien(Long idUser, String hoTen, String sdt, String diaChi, String email, LocalDate ngaySinh, boolean gioiTinh, String image, Long luongThang) {
        super(idUser, hoTen, sdt, diaChi, email, ngaySinh, gioiTinh, image);
        this.luongThang = luongThang;
    }
}
