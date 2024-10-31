package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.LopEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long idLopHoc;
    private Long soHocVien;
    private  String tenLopHoc;
    private LopEnum trangThai;
    private Date ngayBD;
    private Date ngayKT;
    @ManyToOne
    private GiangVien giangVien;
    @ManyToOne(cascade = CascadeType.ALL)
    private KhoaHoc khoaHoc;
//    @OneToMany(mappedBy = "HocVienLopHocKey.lopHoc")
//    private List<HocVienLopHoc> hocVienLopHocs;


    public LopHoc(Long soHocVien, String tenLopHoc, Date ngayBD, Date ngayKT) {
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
    }

    public LopHoc(Long soHocVien, String tenLopHoc) {
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
    }
}
