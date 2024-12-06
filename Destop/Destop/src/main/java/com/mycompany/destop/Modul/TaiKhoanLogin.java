/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import com.mycompany.destop.Enum.ChucVu;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;

/**
 *
 * @author Windows 10
 */
public class TaiKhoanLogin {
    private Long id;
    private String tenDangNhap;
    private String matKhau;
    private Boolean enable;
    @OneToOne
    private User user;
    @Enumerated(value = EnumType.STRING)
    private ChucVu role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChucVu getRole() {
        return role;
    }

    public void setRole(ChucVu role) {
        this.role = role;
    }

    public TaiKhoanLogin(String tenDangNhap, String matKhau, Boolean enable, User user, ChucVu role) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.enable = enable;
        this.user = user;
        this.role = role;
    }

    public TaiKhoanLogin(Long id, String tenDangNhap, String matKhau, Boolean enable, User user, ChucVu role) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.enable = enable;
        this.user = user;
        this.role = role;
    }

    public TaiKhoanLogin() {
    }
    
}
