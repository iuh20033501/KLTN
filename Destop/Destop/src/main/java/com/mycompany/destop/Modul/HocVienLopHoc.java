/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

/**
 *
 * @author Windows 10
 */
public class HocVienLopHoc {
    private HocVienLopHocKey key;
    private Boolean trangThai;

    public HocVienLopHocKey getKey() {
        return key;
    }

    public void setKey(HocVienLopHocKey key) {
        this.key = key;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public HocVienLopHoc() {
    }

    public HocVienLopHoc(HocVienLopHocKey key, Boolean trangThai) {
        this.key = key;
        this.trangThai = trangThai;
    }

    public HocVienLopHoc(Boolean trangThai) {
        this.trangThai = trangThai;
    }
    
}
