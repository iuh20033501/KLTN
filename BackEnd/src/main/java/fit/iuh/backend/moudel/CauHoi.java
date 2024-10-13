package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long idCauHoi;
    private String noiDung;
    private String linkAmThanh;
    private String linkAnh;
    @ManyToOne
    private BaiTap baiTap;
    @ManyToOne
    private BaiTest baiTest;
    private String loiGiai;

    public CauHoi(String noiDung, String linkAmThanh, String linkAnh, BaiTap baiTap, String loiGiai) {
        this.noiDung = noiDung;
        this.linkAmThanh = linkAmThanh;
        this.linkAnh = linkAnh;
        this.baiTap = baiTap;
        this.loiGiai = loiGiai;
    }

    public CauHoi(String noiDung, String linkAmThanh, String linkAnh, BaiTest baiTest, String loiGiai) {
        this.noiDung = noiDung;
        this.linkAmThanh = linkAmThanh;
        this.linkAnh = linkAnh;
        this.baiTest = baiTest;
        this.loiGiai = loiGiai;
    }
}
