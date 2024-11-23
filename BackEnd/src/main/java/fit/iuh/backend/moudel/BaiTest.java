package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.LoaiTest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTest;
    private Date ngayBD;
    private Date ngayKT;
    private String thoiGianLamBai;
    @ManyToOne
    private LopHoc lopHoc;
    private LoaiTest loaiTest;
    private Boolean TrangThai;

    public BaiTest(Date ngayBD, Date ngayKT, String thoiGianLamBai, LoaiTest loaiTest) {
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.thoiGianLamBai = thoiGianLamBai;
        this.loaiTest = loaiTest;
    }
}
