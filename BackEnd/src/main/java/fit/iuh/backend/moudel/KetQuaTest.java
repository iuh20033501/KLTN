package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
public class KetQuaTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKetQua;
    private Long diemTest;
    @ManyToOne
    private BaiTest baiTest;
    @ManyToOne
    private HocVien hocVien;

    public KetQuaTest(Long diemTest, BaiTest baiTest, HocVien hocVien) {
        this.diemTest = diemTest;
        this.baiTest = baiTest;
        this.hocVien = hocVien;
    }
}
