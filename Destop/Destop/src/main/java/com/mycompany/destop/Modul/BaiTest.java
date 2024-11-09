/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import com.mycompany.destop.Enum.TestEnum;
import jakarta.persistence.ManyToOne;
import java.util.Date;

/**
 *
 * @author Windows 10
 */
public class BaiTest {
    private Long idTest;
    private Date ngayBD;
    private Date ngayKT;
    private String thoiGianLamBai;
    @ManyToOne
    private LopHoc lopHoc;
    private TestEnum loaiTest;
    private Boolean TrangThai;

    public Long getIdTest() {
        return idTest;
    }

    public void setIdTest(Long idTest) {
        this.idTest = idTest;
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

    public String getThoiGianLamBai() {
        return thoiGianLamBai;
    }

    public void setThoiGianLamBai(String thoiGianLamBai) {
        this.thoiGianLamBai = thoiGianLamBai;
    }

    public LopHoc getLopHoc() {
        return lopHoc;
    }

    public void setLopHoc(LopHoc lopHoc) {
        this.lopHoc = lopHoc;
    }

    public TestEnum getLoaiTest() {
        return loaiTest;
    }

    public void setLoaiTest(TestEnum loaiTest) {
        this.loaiTest = loaiTest;
    }

    public Boolean getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(Boolean TrangThai) {
        this.TrangThai = TrangThai;
    }

    public BaiTest(Date ngayBD, Date ngayKT, String thoiGianLamBai, TestEnum loaiTest) {
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.thoiGianLamBai = thoiGianLamBai;
        this.loaiTest = loaiTest;
    }

    public BaiTest(Date ngayBD, Date ngayKT, String thoiGianLamBai, LopHoc lopHoc, TestEnum loaiTest, Boolean TrangThai) {
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.thoiGianLamBai = thoiGianLamBai;
        this.lopHoc = lopHoc;
        this.loaiTest = loaiTest;
        this.TrangThai = TrangThai;
    }

    public BaiTest() {
    }
    
    
}
