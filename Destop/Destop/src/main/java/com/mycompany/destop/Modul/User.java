/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Windows 10
 */
public class User {
    private  Long idUser;
    private String hoTen;
    private String sdt;
    private String diaChi;
    private String email;
    private LocalDate ngaySinh;
    private boolean gioiTinh;
    private String image;

    public User() {
    }

    public User(String hoTen, String sdt, String diaChi, String email, LocalDate ngaySinh, boolean gioiTinh, String image) {
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.image = image;
    }

    public User(Long idUser, String hoTen, String sdt, String diaChi, String email, LocalDate ngaySinh, boolean gioiTinh, String image) {
        this.idUser = idUser;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.image = image;
    }

    public User(String hoTen, String sdt, String diaChi, String email, boolean gioiTinh, String image) {
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.image = image;
    }
    

    public Long getIdUser() {
        return idUser;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public String getImage() {
        return image;
    }
    
    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
}
