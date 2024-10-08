package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.SkillEnum;
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
public class  KhoaHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKhoaHoc;
    private String tenKhoaHoc;
    private Long giaTien;
    private  double hocPhi;
    private Date thoiGianDienRa;
    private Boolean trangThai;
    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.ORDINAL)
    private List<SkillEnum> skillEnum;
}
