package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHoaDon;
    private Date ngayLap;
    @ManyToOne
    private NhanVien nguoiLap;
    private Long thanhTien;
    private Boolean trangThai;

    public HoaDon(Date ngayLap, Long thanhTien, Boolean trangThai) {
        this.ngayLap = ngayLap;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }

    public HoaDon(Date ngayLap, NhanVien nguoiLap, Long thanhTien, Boolean trangThai) {
        this.ngayLap = ngayLap;
        this.nguoiLap = nguoiLap;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }
}
