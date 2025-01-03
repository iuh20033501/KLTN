package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
public class KetQuaTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKetQua;
    private Float diemTest;
    private String thoiGianHoanThanh;
//    private Boolean retest;
    @ManyToOne
    private BaiTest baiTest;
    @ManyToOne
    private HocVien hocVien;



    public KetQuaTest(Float diemTest, String thoiGianHoanThanh) {
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
    }

    public KetQuaTest(Float diemTest, String thoiGianHoanThanh, BaiTest baiTest, HocVien hocVien) {
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
        this.baiTest = baiTest;
        this.hocVien = hocVien;
    }


}
