package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.LopEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne
    private GiangVien giangVien;
    @ManyToOne
    private KhoaHoc khoaHoc;
//    @OneToMany(mappedBy = "HocVienLopHocKey.lopHoc")
//    private List<HocVienLopHoc> hocVienLopHocs;

//    public LopHoc(String tenLopHoc, GiangVien giangVien, KhoaHoc khoaHoc) {
//        this.tenLopHoc = tenLopHoc;
//        this.giangVien = giangVien;
//        this.khoaHoc = khoaHoc;
//
//    }
//
//    public LopHoc(Long soHocVien, String tenLopHoc, GiangVien giangVien, KhoaHoc khoaHoc) {
//        this.soHocVien = soHocVien;
//        this.tenLopHoc = tenLopHoc;
//        this.giangVien = giangVien;
//        this.khoaHoc = khoaHoc;
//    }

    public LopHoc(Long soHocVien, String tenLopHoc) {
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
    }
}
