package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TienTrinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTienTrinh;
    private Long phanTram;
    @ManyToOne
    private HocVien hocVien;
    @ManyToOne
    private BaiTap baiTap;
}
