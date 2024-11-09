/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import java.time.LocalDate;

/**
 *
 * @author Windows 10
 */
public class NhanVien extends User{
    private Long luongThang;

    public Long getLuongThang() {
        return luongThang;
    }

    public void setLuongThang(Long luongThang) {
        this.luongThang = luongThang;
    }

    public NhanVien(Long luongThang, String hoTen, String sdt, String diaChi, String email, LocalDate ngaySinh, boolean gioiTinh, String image) {
        super(hoTen, sdt, diaChi, email, ngaySinh, gioiTinh, image);
        this.luongThang = luongThang;
    }

    public NhanVien(Long luongThang, Long idUser, String hoTen, String sdt, String diaChi, String email, LocalDate ngaySinh, boolean gioiTinh, String image) {
        super(idUser, hoTen, sdt, diaChi, email, ngaySinh, gioiTinh, image);
        this.luongThang = luongThang;
    }

    public NhanVien() {
    }
    
}
