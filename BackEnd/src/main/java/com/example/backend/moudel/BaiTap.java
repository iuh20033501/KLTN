package com.example.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String maBaiTap;
    private String tenBaiTap;
    @OneToOne
    private BuoiHoc buoiHoc;
    @ManyToOne
    private HocVienBaiTap hocVien;
}
