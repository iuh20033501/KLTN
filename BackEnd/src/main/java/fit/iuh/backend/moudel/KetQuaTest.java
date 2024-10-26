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
    private Time thoiGianHoanThanh;
    @ManyToOne
    private BaiTest baiTest;
    @ManyToOne
    private HocVien hocVien;



    public KetQuaTest(Long diemTest, Time thoiGianHoanThanh) {
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
    }

    public KetQuaTest(Long diemTest, Time thoiGianHoanThanh, BaiTest baiTest, HocVien hocVien) {
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
        this.baiTest = baiTest;
        this.hocVien = hocVien;
    }
}
