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
<<<<<<< HEAD
<<<<<<< HEAD
    private Time  thoiGianLamBai;
=======
    private Integer thoiGianLamBai;
>>>>>>> 7c175befee12b83998a98cd8fd1cd567600cba2f
=======
    private Integer thoiGianLamBai;
>>>>>>> 7c175befee12b83998a98cd8fd1cd567600cba2f
    @ManyToOne
    private LopHoc lopHoc;
    private LoaiTest loaiTest;
    private Boolean TrangThai;
    private Boolean xetDuyet;


    public BaiTest(Date ngayBD, Date ngayKT, Time  thoiGianLamBai, LoaiTest loaiTest) {
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.thoiGianLamBai = thoiGianLamBai;
        this.loaiTest = loaiTest;
    }

}
