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
//    private String noiDung;
    private String linkLoad;
    @ManyToOne
    private BuoiHoc buoiHoc;
//    private Date ngayMo;
//    private Date ngayDong;
    private Boolean trangThai;

    public TaiLieu(String tenTaiLieu, String linkLoad) {
        this.tenTaiLieu = tenTaiLieu;
        this.linkLoad = linkLoad;
    }

    public TaiLieu(String tenTaiLieu, String linkLoad, Boolean trangThai) {
        this.tenTaiLieu = tenTaiLieu;
        this.linkLoad = linkLoad;
        this.trangThai = trangThai;
    }
}
