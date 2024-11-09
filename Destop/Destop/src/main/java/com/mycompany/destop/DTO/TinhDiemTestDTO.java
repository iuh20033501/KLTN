/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.mycompany.destop.Modul.CauTraLoi;
import java.util.ArrayList;

/**
 *
 * @author Windows 10
 */
public class TinhDiemTestDTO {
    private ArrayList<CauTraLoi> listCauTraLoi;
    private String thoigianLamBai;
    private Long idBaiTest;
    private Long idHocVien;

    public ArrayList<CauTraLoi> getListCauTraLoi() {
        return listCauTraLoi;
    }

    public void setListCauTraLoi(ArrayList<CauTraLoi> listCauTraLoi) {
        this.listCauTraLoi = listCauTraLoi;
    }

    public String getThoigianLamBai() {
        return thoigianLamBai;
    }

    public void setThoigianLamBai(String thoigianLamBai) {
        this.thoigianLamBai = thoigianLamBai;
    }

    public Long getIdBaiTest() {
        return idBaiTest;
    }

    public void setIdBaiTest(Long idBaiTest) {
        this.idBaiTest = idBaiTest;
    }

    public Long getIdHocVien() {
        return idHocVien;
    }

    public void setIdHocVien(Long idHocVien) {
        this.idHocVien = idHocVien;
    }

    public TinhDiemTestDTO() {
    }

    public TinhDiemTestDTO(ArrayList<CauTraLoi> listCauTraLoi, String thoigianLamBai, Long idBaiTest, Long idHocVien) {
        this.listCauTraLoi = listCauTraLoi;
        this.thoigianLamBai = thoigianLamBai;
        this.idBaiTest = idBaiTest;
        this.idHocVien = idHocVien;
    }

    public TinhDiemTestDTO(ArrayList<CauTraLoi> listCauTraLoi, String thoigianLamBai) {
        this.listCauTraLoi = listCauTraLoi;
        this.thoigianLamBai = thoigianLamBai;
    }
    
}
