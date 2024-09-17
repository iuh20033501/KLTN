package com.example.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String maHoaDon;
    private Date ngayLap;
    @ManyToOne
    private NhanVien nguoiLap;
    @ManyToOne
    private HocVien nguoiThanhToan;
    private Long thanhTien;
    private Boolean trangThai;
}
