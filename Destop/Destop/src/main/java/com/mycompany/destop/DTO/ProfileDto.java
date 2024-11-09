/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.mycompany.destop.Modul.TaiKhoanLogin;

/**
 *
 * @author Windows 10
 */
public class ProfileDto {
     private TaiKhoanLogin tk;
    private String token;
    private String refreshToken;

    public TaiKhoanLogin getTk() {
        return tk;
    }

    public void setTk(TaiKhoanLogin tk) {
        this.tk = tk;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public ProfileDto() {
    }

    public ProfileDto(TaiKhoanLogin tk, String token, String refreshToken) {
        this.tk = tk;
        this.token = token;
        this.refreshToken = refreshToken;
    }
    
}
