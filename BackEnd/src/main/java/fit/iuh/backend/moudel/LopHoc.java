package fit.iuh.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long idLopHoc;
    private  String tenLopHoc;
    @ManyToOne
    private GiangVien giangVien;
    @ManyToOne
    private KhoaHoc khoaHoc;
}