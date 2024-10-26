package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
public class KetQuaTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKetQua;
    private Long diemTest;
    private String thoiGianHoanThanh;
    @ManyToOne
    private BaiTest baiTest;
    @ManyToOne
    private HocVien hocVien;



    public KetQuaTest(Long diemTest, String thoiGianHoanThanh) {
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
    }

    public KetQuaTest(Long diemTest, String thoiGianHoanThanh, BaiTest baiTest, HocVien hocVien) {
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
        this.baiTest = baiTest;
        this.hocVien = hocVien;
    }
}
