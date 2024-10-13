package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.ThanhToanEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTT;
    private ThanhToanEnum trangThai;
    @ManyToOne
    private HocVien nguoiThanhToan;
    @ManyToOne
    private HoaDon hoaDon;
    @ManyToOne
    private LopHoc lopHoc;

    public ThanhToan(ThanhToanEnum trangThai, HocVien nguoiThanhToan, HoaDon hoaDon, LopHoc lopHoc) {
        this.trangThai = trangThai;
        this.nguoiThanhToan = nguoiThanhToan;
        this.hoaDon = hoaDon;
        this.lopHoc = lopHoc;
    }
}
