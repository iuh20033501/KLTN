package fit.iuh.backend.moudel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocVienBaiTest {
    @Id
    @ManyToOne
    private HocVien idHocVien;
    @Id
    @ManyToOne
    private BaiTest  idBaiTest;
}
