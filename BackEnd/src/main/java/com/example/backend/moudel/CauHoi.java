package com.example.backend.moudel;

import com.example.backend.enumclass.QuestionEnum;
import com.example.backend.enumclass.SkillEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long idCauHoi;
    private String noiDung;
    private String linkAmThanh;
    private String linkAnh;
    private QuestionEnum loaiCau;
    @ManyToOne
    private BaiTap baiTap;
    @ManyToOne
    private BaiTest baiTest;
    private String loiGiai;
}
