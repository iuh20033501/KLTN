package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBaiTap;
    private String tenBaiTap;
    @OneToOne
    private BuoiHoc buoiHoc;
    private Date ngayBD;
    private Date ngayKT;
    private Boolean trangThai;
    public BaiTap(String tenBaiTap, Date ngayBD, Date ngayKT) {
        this.tenBaiTap = tenBaiTap;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
    }

    public BaiTap(String tenBaiTap, Date ngayBD, Date ngayKT, Boolean trangThai) {
        this.tenBaiTap = tenBaiTap;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = trangThai;
    }
}
