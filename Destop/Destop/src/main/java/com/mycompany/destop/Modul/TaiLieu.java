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
public class TaiLieu {
    private Long idTaiLieu;
    private String tenTaiLieu;
    private String noiDung;
    private String linkLoad;
    @OneToOne
    private BuoiHoc buoiHoc;
    private Date ngayMo;
    private Date ngayDong;
    private Boolean trangThai;

    public Long getIdTaiLieu() {
        return idTaiLieu;
    }

    public void setIdTaiLieu(Long idTaiLieu) {
        this.idTaiLieu = idTaiLieu;
    }

    public String getTenTaiLieu() {
        return tenTaiLieu;
    }

    public void setTenTaiLieu(String tenTaiLieu) {
        this.tenTaiLieu = tenTaiLieu;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getLinkLoad() {
        return linkLoad;
    }

    public void setLinkLoad(String linkLoad) {
        this.linkLoad = linkLoad;
    }

    public BuoiHoc getBuoiHoc() {
        return buoiHoc;
    }

    public void setBuoiHoc(BuoiHoc buoiHoc) {
        this.buoiHoc = buoiHoc;
    }

    public Date getNgayMo() {
        return ngayMo;
    }

    public void setNgayMo(Date ngayMo) {
        this.ngayMo = ngayMo;
    }

    public Date getNgayDong() {
        return ngayDong;
    }

    public void setNgayDong(Date ngayDong) {
        this.ngayDong = ngayDong;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public TaiLieu(String tenTaiLieu, String noiDung, String linkLoad, Date ngayMo, Date ngayDong, Boolean trangThai) {
        this.tenTaiLieu = tenTaiLieu;
        this.noiDung = noiDung;
        this.linkLoad = linkLoad;
        this.ngayMo = ngayMo;
        this.ngayDong = ngayDong;
        this.trangThai = trangThai;
    }

    public TaiLieu() {
    }

    public TaiLieu(Long idTaiLieu, String tenTaiLieu, String noiDung, String linkLoad, BuoiHoc buoiHoc, Date ngayMo, Date ngayDong, Boolean trangThai) {
        this.idTaiLieu = idTaiLieu;
        this.tenTaiLieu = tenTaiLieu;
        this.noiDung = noiDung;
        this.linkLoad = linkLoad;
        this.buoiHoc = buoiHoc;
        this.ngayMo = ngayMo;
        this.ngayDong = ngayDong;
        this.trangThai = trangThai;
    }

    public TaiLieu(String tenTaiLieu, String noiDung, String linkLoad, BuoiHoc buoiHoc, Date ngayMo, Date ngayDong, Boolean trangThai) {
        this.tenTaiLieu = tenTaiLieu;
        this.noiDung = noiDung;
        this.linkLoad = linkLoad;
        this.buoiHoc = buoiHoc;
        this.ngayMo = ngayMo;
        this.ngayDong = ngayDong;
        this.trangThai = trangThai;
    }
    
}
