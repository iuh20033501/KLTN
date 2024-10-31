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
    private Boolean HocOnl;
    private String noiHoc;
    private String gioHoc;
    private String gioKetThuc;
    @ManyToOne
    private LopHoc lopHoc;

    public BuoiHoc(String chuDe, Date ngayHoc, Boolean hocOnl, String noiHoc, String gioHoc, String gioKetThuc) {
        this.chuDe = chuDe;
        this.ngayHoc = ngayHoc;
        HocOnl = hocOnl;
        this.noiHoc = noiHoc;
        this.gioHoc = gioHoc;
        this.gioKetThuc = gioKetThuc;
    }
}
