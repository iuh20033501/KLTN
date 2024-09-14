package com.example.backend.moudel;

import com.example.backend.enumclass.SkillEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhoaHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String maKhoaHoc;
    private String tenKhoaHoc;
    @ManyToOne
    private LopHoc dsLopHoc;
    private  double hocPhi;
    private Date thoiGianDienRa;
    @Enumerated(EnumType.ORDINAL)
    private SkillEnum skillEnum;


}
