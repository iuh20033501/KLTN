/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import jakarta.persistence.ManyToOne;

/**
 *
 * @author Windows 10
 */
public class TienTrinh {
    private Long idTienTrinh;
    private Long cauDung;
    @ManyToOne
    private HocVien hocVien;
    @ManyToOne
    private BaiTap baiTap;

    public TienTrinh(Long cauDung) {
        this.cauDung = cauDung;
    }

    public TienTrinh() {
    }

    public TienTrinh(Long cauDung, HocVien hocVien, BaiTap baiTap) {
        this.cauDung = cauDung;
        this.hocVien = hocVien;
        this.baiTap = baiTap;
    }
    
    public TienTrinh(Long idTienTrinh, Long cauDung, HocVien hocVien, BaiTap baiTap) {
        this.idTienTrinh = idTienTrinh;
        this.cauDung = cauDung;
        this.hocVien = hocVien;
        this.baiTap = baiTap;
    }

    public Long getIdTienTrinh() {
        return idTienTrinh;
    }

    public void setIdTienTrinh(Long idTienTrinh) {
        this.idTienTrinh = idTienTrinh;
    }

    public Long getCauDung() {
        return cauDung;
    }

    public void setCauDung(Long cauDung) {
        this.cauDung = cauDung;
    }

    public HocVien getHocVien() {
        return hocVien;
    }

    public void setHocVien(HocVien hocVien) {
        this.hocVien = hocVien;
    }

    public BaiTap getBaiTap() {
        return baiTap;
    }

    public void setBaiTap(BaiTap baiTap) {
        this.baiTap = baiTap;
    }
    
    
}
