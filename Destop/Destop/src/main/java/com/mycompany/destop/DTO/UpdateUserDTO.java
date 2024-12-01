/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.mycompany.destop.Modul.NhanVien;

/**
 *
 * @author Windows 10
 */
public class UpdateUserDTO {

    private NhanVien nhanVien;
    private String gson;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(NhanVien nhanVien, String gson) {
        this.nhanVien = nhanVien;
        this.gson = gson;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getGson() {
        return gson;
    }

    public void setGson(String gson) {
        this.gson = gson;
    }
    
}
