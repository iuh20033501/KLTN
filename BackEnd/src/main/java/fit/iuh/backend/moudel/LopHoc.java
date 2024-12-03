package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.TrangThaiLop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private TrangThaiLop trangThai;
    private Date ngayBD;
    private Date ngayKT;
    @ManyToOne
    private GiangVien giangVien;
    @ManyToOne(cascade = CascadeType.ALL)
    private KhoaHoc khoaHoc;
//    @OneToMany(mappedBy = "HocVienLopHocKey.lopHoc")
//    private List<HocVienLopHoc> hocVienLopHocs;
    private  String moTa;

    public LopHoc(Long idLopHoc, Long soHocVien, String tenLopHoc, TrangThaiLop trangThai, String moTa) {
        this.idLopHoc = idLopHoc;
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
        this.trangThai = trangThai;
        this.moTa = moTa;
    }

    public LopHoc(Long soHocVien, String tenLopHoc, TrangThaiLop trangThai, String moTa) {
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
        this.trangThai = trangThai;
        this.moTa = moTa;
    }

    public LopHoc(Long soHocVien, String tenLopHoc, Date ngayBD, Date ngayKT) {
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
    }

    public LopHoc(Long soHocVien, String tenLopHoc, Date ngayBD, Date ngayKT, String moTa) {
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.moTa = moTa;
    }

    public LopHoc(Long soHocVien, String tenLopHoc) {
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
    }
}
