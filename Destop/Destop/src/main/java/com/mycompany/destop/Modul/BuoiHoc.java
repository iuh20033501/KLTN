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
    private Boolean HocOnl;
    private String noiHoc;
    private String gioHoc;
    private String gioKetThuc;
    @ManyToOne
    private LopHoc lopHoc;

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
        return HocOnl;
    }

    public void setHocOnl(Boolean HocOnl) {
        this.HocOnl = HocOnl;
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

    public BuoiHoc(String chuDe, Date ngayHoc, Boolean HocOnl, String noiHoc, String gioHoc, String gioKetThuc) {
        this.chuDe = chuDe;
        this.ngayHoc = ngayHoc;
        this.HocOnl = HocOnl;
        this.noiHoc = noiHoc;
        this.gioHoc = gioHoc;
        this.gioKetThuc = gioKetThuc;
    }

    public BuoiHoc(String chuDe, Date ngayHoc, Boolean HocOnl, String noiHoc, String gioHoc, String gioKetThuc, LopHoc lopHoc) {
        this.chuDe = chuDe;
        this.ngayHoc = ngayHoc;
        this.HocOnl = HocOnl;
        this.noiHoc = noiHoc;
        this.gioHoc = gioHoc;
        this.gioKetThuc = gioKetThuc;
        this.lopHoc = lopHoc;
    }

    public BuoiHoc(Long idBuoiHoc, String chuDe, Date ngayHoc, Boolean HocOnl, String noiHoc, String gioHoc, String gioKetThuc, LopHoc lopHoc) {
        this.idBuoiHoc = idBuoiHoc;
        this.chuDe = chuDe;
        this.ngayHoc = ngayHoc;
        this.HocOnl = HocOnl;
        this.noiHoc = noiHoc;
        this.gioHoc = gioHoc;
        this.gioKetThuc = gioKetThuc;
        this.lopHoc = lopHoc;
    }

    public BuoiHoc() {
    }
    
}
