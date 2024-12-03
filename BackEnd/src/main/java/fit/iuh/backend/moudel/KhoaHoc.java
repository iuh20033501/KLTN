package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.Skill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @ElementCollection(targetClass = Skill.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "khoa_hoc_skills", joinColumns = @JoinColumn(name = "khoa_hoc_id"))
    @Column(name = "skill")
    private List<Skill> skill;

}
