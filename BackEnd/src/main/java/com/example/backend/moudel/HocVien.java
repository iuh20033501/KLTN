package com.example.backend.moudel;

import com.example.backend.enumclass.SkillEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocVien extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  String maHocVien;
    private String tenHocVien;
    private SkillEnum kiNangCan;
}
