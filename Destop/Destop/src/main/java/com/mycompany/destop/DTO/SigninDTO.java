/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.mycompany.destop.Enum.ChucVuEnum;
import com.mycompany.destop.Modul.User;

/**
 *
 * @author Windows 10
 */
public class SigninDTO {
    private User u;
    private ChucVuEnum cvEnum;

    // Getter cho User
    public User getU() {
        return u;
    }

    // Setter cho User
    public void setU(User u) {
        this.u = u;
    }

    // Getter cho ChucVuEnum
    public ChucVuEnum getCvEnum() {
        return cvEnum;
    }

    // Setter cho ChucVuEnum
    public void setCvEnum(ChucVuEnum cvEnum) {
        this.cvEnum = cvEnum;
    }
}

