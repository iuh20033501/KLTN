/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.mycompany.destop.Modul.BuoiHoc;

/**
 *
 * @author Windows 10
 */
public class CreateBuoiDTO {
     private String NgayHoc;
    private BuoiHoc buoiHoc;

    public CreateBuoiDTO(String NgayHoc, BuoiHoc buoiHoc) {
        this.NgayHoc = NgayHoc;
        this.buoiHoc = buoiHoc;
    }

    public CreateBuoiDTO() {
    }

    public String getNgayHoc() {
        return NgayHoc;
    }

    public void setNgayHoc(String NgayHoc) {
        this.NgayHoc = NgayHoc;
    }

    public BuoiHoc getBuoiHoc() {
        return buoiHoc;
    }

    public void setBuoiHoc(BuoiHoc buoiHoc) {
        this.buoiHoc = buoiHoc;
    }
    
    
}
