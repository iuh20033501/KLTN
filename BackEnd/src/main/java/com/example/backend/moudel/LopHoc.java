package com.example.backend.moudel;

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
    private  String maLopHoc;
    private  String tenLopHoc;
    @ManyToOne
    private GiangVien giangVien;
    @ManyToOne
    private KhoaHoc khoaHoc;
}
