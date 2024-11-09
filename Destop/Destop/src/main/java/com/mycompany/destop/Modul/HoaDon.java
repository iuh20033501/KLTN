/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import jakarta.persistence.ManyToOne;
import java.util.Date;

/**
 *
 * @author Windows 10
 */
public class HoaDon {
    private Long idHoaDon;
    private Date ngayLap;
    @ManyToOne
    private NhanVien nguoiLap;
    private Long thanhTien;
    private Boolean trangThai;

    public Long getIdHoaDon() {
        return idHoaDon;
    }

    public void setIdHoaDon(Long idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public NhanVien getNguoiLap() {
        return nguoiLap;
    }

    public void setNguoiLap(NhanVien nguoiLap) {
        this.nguoiLap = nguoiLap;
    }

    public Long getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(Long thanhTien) {
        this.thanhTien = thanhTien;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public HoaDon() {
    }

    public HoaDon(Long idHoaDon, Date ngayLap, NhanVien nguoiLap, Long thanhTien, Boolean trangThai) {
        this.idHoaDon = idHoaDon;
        this.ngayLap = ngayLap;
        this.nguoiLap = nguoiLap;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }

    public HoaDon(Date ngayLap, Long thanhTien, Boolean trangThai) {
        this.ngayLap = ngayLap;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }

    public HoaDon(Date ngayLap, Long thanhTien) {
        this.ngayLap = ngayLap;
        this.thanhTien = thanhTien;
    }

    public HoaDon(Date ngayLap, NhanVien nguoiLap, Long thanhTien, Boolean trangThai) {
        this.ngayLap = ngayLap;
        this.nguoiLap = nguoiLap;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
    }
    
    
}
