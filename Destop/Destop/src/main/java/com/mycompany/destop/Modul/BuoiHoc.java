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
public class BuoiHoc {
     private Long idBuoiHoc;
    private String chuDe;
    private Date ngayHoc;
    private Boolean hocOnl;
    private String noiHoc;
    private String gioHoc;
    private String gioKetThuc;
    @ManyToOne
    private LopHoc lopHoc;
     private Boolean trangThai;

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }
     

    public Long getIdBuoiHoc() {
        return idBuoiHoc;
    }

    public void setIdBuoiHoc(Long idBuoiHoc) {
        this.idBuoiHoc = idBuoiHoc;
    }

    public String getChuDe() {
        return chuDe;
    }

    public void setChuDe(String chuDe) {
        this.chuDe = chuDe;
    }

    public Date getNgayHoc() {
        return ngayHoc;
    }

    public void setNgayHoc(Date ngayHoc) {
        this.ngayHoc = ngayHoc;
    }

    public Boolean getHocOnl() {
        return hocOnl;
    }

    public void setHocOnl(Boolean HocOnl) {
        this.hocOnl = HocOnl;
    }

    public String getNoiHoc() {
        return noiHoc;
    }

    public void setNoiHoc(String noiHoc) {
        this.noiHoc = noiHoc;
    }

    public String getGioHoc() {
        return gioHoc;
    }

    public void setGioHoc(String gioHoc) {
        this.gioHoc = gioHoc;
    }

    public String getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(String gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public LopHoc getLopHoc() {
        return lopHoc;
    }

    public void setLopHoc(LopHoc lopHoc) {
        this.lopHoc = lopHoc;
    }

    public BuoiHoc(Long idBuoiHoc, String chuDe, Date ngayHoc, Boolean hocOnl, String noiHoc, String gioHoc, String gioKetThuc, LopHoc lopHoc, Boolean trangThai) {
        this.idBuoiHoc = idBuoiHoc;
        this.chuDe = chuDe;
        this.ngayHoc = ngayHoc;
        this.hocOnl = hocOnl;
        this.noiHoc = noiHoc;
        this.gioHoc = gioHoc;
        this.gioKetThuc = gioKetThuc;
        this.lopHoc = lopHoc;
        this.trangThai = trangThai;
    }

    public BuoiHoc(String chuDe, Boolean hocOnl, String noiHoc, String gioHoc, String gioKetThuc, Boolean trangThai) {
        this.chuDe = chuDe;
        this.hocOnl = hocOnl;
        this.noiHoc = noiHoc;
        this.gioHoc = gioHoc;
        this.gioKetThuc = gioKetThuc;
       
        this.trangThai = trangThai;
    }
    public BuoiHoc() {
    }
    
}
