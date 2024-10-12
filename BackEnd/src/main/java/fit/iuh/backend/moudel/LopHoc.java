package fit.iuh.backend.moudel;

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
    private  String tenLopHoc;
    private Boolean trangThai;
    @ManyToOne
    private GiangVien giangVien;
    @ManyToOne
    private KhoaHoc khoaHoc;
//    @OneToMany(mappedBy = "HocVienLopHocKey.lopHoc")
//    private List<HocVienLopHoc> hocVienLopHocs;

}
