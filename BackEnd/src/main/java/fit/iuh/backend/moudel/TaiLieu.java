package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TaiLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTaiLieu;
    private String tenTaiLieu;
    private String noiDung;
    private String linkLoad;
    @ManyToOne
    private BuoiHoc buoiHoc;
    private Date ngayMo;
    private Date ngayDong;
    private Boolean trangThai;

    public TaiLieu(String tenTaiLieu, String noiDung, String linkLoad, Date ngayMo, Date ngayDong, Boolean trangThai) {
        this.tenTaiLieu = tenTaiLieu;
        this.noiDung = noiDung;
        this.linkLoad = linkLoad;
        this.ngayMo = ngayMo;
        this.ngayDong = ngayDong;
        this.trangThai = trangThai;
    }

    public TaiLieu(String tenTaiLieu, String noiDung, String linkLoad, Date ngayMo, Date ngayDong) {
        this.tenTaiLieu = tenTaiLieu;
        this.noiDung = noiDung;
        this.linkLoad = linkLoad;
        this.ngayMo = ngayMo;
        this.ngayDong = ngayDong;
    }
}
