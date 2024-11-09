/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import jakarta.persistence.ManyToOne;
import java.io.Serializable;

/**
 *
 * @author Windows 10
 */
public class HocVienLopHocKey implements Serializable {
    @ManyToOne
    private HocVien hocVien;
    @ManyToOne
    private LopHoc  lopHoc;

    public HocVien getHocVien() {
        return hocVien;
    }

    public void setHocVien(HocVien hocVien) {
        this.hocVien = hocVien;
    }

    public LopHoc getLopHoc() {
        return lopHoc;
    }

    public void setLopHoc(LopHoc lopHoc) {
        this.lopHoc = lopHoc;
    }

    public HocVienLopHocKey() {
    }

    public HocVienLopHocKey(HocVien hocVien, LopHoc lopHoc) {
        this.hocVien = hocVien;
        this.lopHoc = lopHoc;
    }
    
}
