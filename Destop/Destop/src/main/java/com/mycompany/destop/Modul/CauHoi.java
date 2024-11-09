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
public class CauHoi {
    private  Long idCauHoi;
    private String noiDung;
    private String linkAmThanh;
    private String linkAnh;
    @ManyToOne
    private BaiTap baiTap;
    @ManyToOne
    private BaiTest baiTest;
    private String loiGiai;

    public CauHoi() {
    }

    public CauHoi(String noiDung, String linkAmThanh, String linkAnh, BaiTap baiTap, String loiGiai) {
        this.noiDung = noiDung;
        this.linkAmThanh = linkAmThanh;
        this.linkAnh = linkAnh;
        this.baiTap = baiTap;
        this.loiGiai = loiGiai;
    }

    public CauHoi(String noiDung, String linkAmThanh, String linkAnh, BaiTest baiTest, String loiGiai) {
        this.noiDung = noiDung;
        this.linkAmThanh = linkAmThanh;
        this.linkAnh = linkAnh;
        this.baiTest = baiTest;
        this.loiGiai = loiGiai;
    }

    public CauHoi(Long idCauHoi, String noiDung, String linkAmThanh, String linkAnh, BaiTap baiTap, BaiTest baiTest, String loiGiai) {
        this.idCauHoi = idCauHoi;
        this.noiDung = noiDung;
        this.linkAmThanh = linkAmThanh;
        this.linkAnh = linkAnh;
        this.baiTap = baiTap;
        this.baiTest = baiTest;
        this.loiGiai = loiGiai;
    }

    public Long getIdCauHoi() {
        return idCauHoi;
    }

    public void setIdCauHoi(Long idCauHoi) {
        this.idCauHoi = idCauHoi;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getLinkAmThanh() {
        return linkAmThanh;
    }

    public void setLinkAmThanh(String linkAmThanh) {
        this.linkAmThanh = linkAmThanh;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public BaiTap getBaiTap() {
        return baiTap;
    }

    public void setBaiTap(BaiTap baiTap) {
        this.baiTap = baiTap;
    }

    public BaiTest getBaiTest() {
        return baiTest;
    }

    public void setBaiTest(BaiTest baiTest) {
        this.baiTest = baiTest;
    }

    public String getLoiGiai() {
        return loiGiai;
    }

    public void setLoiGiai(String loiGiai) {
        this.loiGiai = loiGiai;
    }
    
    
}
