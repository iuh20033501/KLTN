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
public class KetQuaTest {
    private Long idKetQua;
    private Float diemTest;
    private String thoiGianHoanThanh;
    @ManyToOne
    private BaiTest baiTest;
    @ManyToOne
    private HocVien hocVien;

    public Long getIdKetQua() {
        return idKetQua;
    }

    public void setIdKetQua(Long idKetQua) {
        this.idKetQua = idKetQua;
    }

    public Float getDiemTest() {
        return diemTest;
    }

    public void setDiemTest(Float diemTest) {
        this.diemTest = diemTest;
    }

   

    public String getThoiGianHoanThanh() {
        return thoiGianHoanThanh;
    }

    public void setThoiGianHoanThanh(String thoiGianHoanThanh) {
        this.thoiGianHoanThanh = thoiGianHoanThanh;
    }

    public BaiTest getBaiTest() {
        return baiTest;
    }

    public void setBaiTest(BaiTest baiTest) {
        this.baiTest = baiTest;
    }

    public HocVien getHocVien() {
        return hocVien;
    }

    public void setHocVien(HocVien hocVien) {
        this.hocVien = hocVien;
    }

    public KetQuaTest(Float diemTest, String thoiGianHoanThanh) {
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
    }

    public KetQuaTest(Float diemTest, String thoiGianHoanThanh, BaiTest baiTest, HocVien hocVien) {
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
        this.baiTest = baiTest;
        this.hocVien = hocVien;
    }

    public KetQuaTest(Long idKetQua, Float diemTest, String thoiGianHoanThanh, BaiTest baiTest, HocVien hocVien) {
        this.idKetQua = idKetQua;
        this.diemTest = diemTest;
        this.thoiGianHoanThanh = thoiGianHoanThanh;
        this.baiTest = baiTest;
        this.hocVien = hocVien;
    }

    public KetQuaTest() {
    }
    
}
