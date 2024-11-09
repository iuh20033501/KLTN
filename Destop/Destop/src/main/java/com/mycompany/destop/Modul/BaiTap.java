/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import jakarta.persistence.OneToOne;
import java.util.Date;

/**
 *
 * @author Windows 10
 */
public class BaiTap {
    private Long idBaiTap;
    private String tenBaiTap;
    @OneToOne
    private BuoiHoc buoiHoc;
    private Date ngayBD;
    private Date ngayKT;
    private Boolean trangThai;

    public Long getIdBaiTap() {
        return idBaiTap;
    }

    public void setIdBaiTap(Long idBaiTap) {
        this.idBaiTap = idBaiTap;
    }

    public String getTenBaiTap() {
        return tenBaiTap;
    }

    public void setTenBaiTap(String tenBaiTap) {
        this.tenBaiTap = tenBaiTap;
    }

    public BuoiHoc getBuoiHoc() {
        return buoiHoc;
    }

    public void setBuoiHoc(BuoiHoc buoiHoc) {
        this.buoiHoc = buoiHoc;
    }

    public Date getNgayBD() {
        return ngayBD;
    }

    public void setNgayBD(Date ngayBD) {
        this.ngayBD = ngayBD;
    }

    public Date getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(Date ngayKT) {
        this.ngayKT = ngayKT;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public BaiTap() {
    }

    public BaiTap(String tenBaiTap, Date ngayBD, Date ngayKT) {
        this.tenBaiTap = tenBaiTap;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
    }

    public BaiTap(Long idBaiTap, String tenBaiTap, BuoiHoc buoiHoc, Date ngayBD, Date ngayKT, Boolean trangThai) {
        this.idBaiTap = idBaiTap;
        this.tenBaiTap = tenBaiTap;
        this.buoiHoc = buoiHoc;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.trangThai = trangThai;
    }
    
    
}
