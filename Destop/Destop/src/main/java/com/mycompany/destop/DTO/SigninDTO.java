/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.google.gson.annotations.SerializedName;
import com.mycompany.destop.Enum.ChucVu;
import com.mycompany.destop.Modul.User;

/**
 *
 * @author Windows 10
 */
public class SigninDTO {
//    @SerializedName("u")
    private User u;
    private ChucVu cvEnum;

    // Getter cho User
    public User getU() {
        return u;
    }

    // Setter cho User
    public void setU(User u) {
        this.u = u;
    }

    // Getter cho ChucVuEnum
    public ChucVu getCvEnum() {
        return cvEnum;
    }

    // Setter cho ChucVuEnum
    public void setCvEnum(ChucVu cvEnum) {
        this.cvEnum = cvEnum;
    }
}

