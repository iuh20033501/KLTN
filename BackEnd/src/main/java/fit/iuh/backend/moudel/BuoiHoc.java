package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BuoiHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBuoiHoc;
    private String chuDe;
    private Date ngayHoc;
    private Boolean hocOnl;
    private String noiHoc;
    private String gioHoc;
    private String gioKetThuc;
    @ManyToOne
    private LopHoc lopHoc;
    private Boolean trangThai;

    public BuoiHoc(String chuDe, Date ngayHoc, Boolean hocOnl, String noiHoc, String gioHoc, String gioKetThuc) {
        this.chuDe = chuDe;
        this.ngayHoc = ngayHoc;
        this.hocOnl = hocOnl;
        this.noiHoc = noiHoc;
        this.gioHoc = gioHoc;
        this.gioKetThuc = gioKetThuc;
    }
}
