package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.SkillEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
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
    private String thoiGianDienRa;
    private Boolean trangThai;
    private Long soBuoi;
    private String moTa;
    @Column(length = 1000000000)
    private String image;
    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.ORDINAL)
    private List<SkillEnum> skillEnum;
}
