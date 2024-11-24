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
    private Long cauDung;
    private  Long cauDaLam;
    @ManyToOne
    private HocVien hocVien;
    @ManyToOne
    private BaiTap baiTap;

    public TienTrinh(Long cauDung, HocVien hocVien, BaiTap baiTap) {
        this.cauDung = cauDung;
        this.hocVien = hocVien;
        this.baiTap = baiTap;
    }

    public TienTrinh(Long cauDung, Long cauLam, HocVien hocVien, BaiTap baiTap) {
        this.cauDung = cauDung;
        this.cauDaLam = cauLam;
        this.hocVien = hocVien;
        this.baiTap = baiTap;
    }

    public TienTrinh(Long cauDung, Long cauDaLam) {
        this.cauDung = cauDung;
        this.cauDaLam = cauDaLam;
    }
}
