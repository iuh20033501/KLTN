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
public class CauTraLoi {
     private Long idCauTraLoi;
    private String noiDung;
    private Boolean ketQua;
    @ManyToOne
    private CauHoi cauHoi;

    public Long getIdCauTraLoi() {
        return idCauTraLoi;
    }

    public void setIdCauTraLoi(Long idCauTraLoi) {
        this.idCauTraLoi = idCauTraLoi;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Boolean getKetQua() {
        return ketQua;
    }

    public void setKetQua(Boolean ketQua) {
        this.ketQua = ketQua;
    }

    public CauHoi getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(CauHoi cauHoi) {
        this.cauHoi = cauHoi;
    }

    public CauTraLoi() {
    }

    public CauTraLoi(Long idCauTraLoi, String noiDung, Boolean ketQua, CauHoi cauHoi) {
        this.idCauTraLoi = idCauTraLoi;
        this.noiDung = noiDung;
        this.ketQua = ketQua;
        this.cauHoi = cauHoi;
    }

    public CauTraLoi(String noiDung, Boolean ketQua) {
        this.noiDung = noiDung;
        this.ketQua = ketQua;
    }

    public CauTraLoi(String noiDung, Boolean ketQua, CauHoi cauHoi) {
        this.noiDung = noiDung;
        this.ketQua = ketQua;
        this.cauHoi = cauHoi;
    }
    
    
}
