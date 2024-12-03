/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.mycompany.destop.Modul.LopHoc;

/**
 *
 * @author Windows 10
 */
public class CreateLopDTO {

    private String NgayBD;
    private String NgayKT;
    private LopHoc lop;

    public CreateLopDTO(String NgayBD, String NgayKT, LopHoc lop) {
        this.NgayBD = NgayBD;
        this.NgayKT = NgayKT;
        this.lop = lop;
    }

    public CreateLopDTO() {
    }

    public String getNgayBD() {
        return NgayBD;
    }

    public void setNgayBD(String NgayBD) {
        this.NgayBD = NgayBD;
    }

    public String getNgayKT() {
        return NgayKT;
    }

    public void setNgayKT(String NgayKT) {
        this.NgayKT = NgayKT;
    }

    public LopHoc getLop() {
        return lop;
    }

    public void setLop(LopHoc lop) {
        this.lop = lop;
    }

}
