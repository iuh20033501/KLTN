package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.LoaiTest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
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

    private Integer thoiGianLamBai;
    @ManyToOne
    private LopHoc lopHoc;
    private LoaiTest loaiTest;
    private Boolean TrangThai;
    private Boolean xetDuyet;


    public BaiTest(Date ngayBD, Date ngayKT, Integer  thoiGianLamBai, LoaiTest loaiTest) {
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.thoiGianLamBai = thoiGianLamBai;
        this.loaiTest = loaiTest;
    }

}
