package com.example.backend.moudel;

import com.example.backend.enumclass.SkillEnum;
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
public class KhoaHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idKhoaHoc;
    private String tenKhoaHoc;
    private Long giaTien;
    private  double hocPhi;
    private Date thoiGianDienRa;

    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.ORDINAL)
    private List<SkillEnum> skillEnum;
}
