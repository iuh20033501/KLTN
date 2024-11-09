/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import com.mycompany.destop.Enum.ThanhToanEnum;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author Windows 10
 */
public class ThanhToan {
    private Long idTT;
    private ThanhToanEnum trangThai;
    @ManyToOne
    private HocVien nguoiThanhToan;
    @ManyToOne
    private HoaDon hoaDon;
    @ManyToOne
    private LopHoc lopHoc;

    public Long getIdTT() {
        return idTT;
    }

    public void setIdTT(Long idTT) {
        this.idTT = idTT;
    }

    public ThanhToanEnum getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(ThanhToanEnum trangThai) {
        this.trangThai = trangThai;
    }

    public HocVien getNguoiThanhToan() {
        return nguoiThanhToan;
    }

    public void setNguoiThanhToan(HocVien nguoiThanhToan) {
        this.nguoiThanhToan = nguoiThanhToan;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public LopHoc getLopHoc() {
        return lopHoc;
    }

    public void setLopHoc(LopHoc lopHoc) {
        this.lopHoc = lopHoc;
    }

    public ThanhToan() {
    }

    public ThanhToan(Long idTT, ThanhToanEnum trangThai, HocVien nguoiThanhToan, HoaDon hoaDon, LopHoc lopHoc) {
        this.idTT = idTT;
        this.trangThai = trangThai;
        this.nguoiThanhToan = nguoiThanhToan;
        this.hoaDon = hoaDon;
        this.lopHoc = lopHoc;
    }

    public ThanhToan(ThanhToanEnum trangThai, HocVien nguoiThanhToan, HoaDon hoaDon, LopHoc lopHoc) {
        this.trangThai = trangThai;
        this.nguoiThanhToan = nguoiThanhToan;
        this.hoaDon = hoaDon;
        this.lopHoc = lopHoc;
    }

    public ThanhToan(ThanhToanEnum trangThai, HocVien nguoiThanhToan, LopHoc lopHoc) {
        this.trangThai = trangThai;
        this.nguoiThanhToan = nguoiThanhToan;
        this.lopHoc = lopHoc;
    }
    
    
}
