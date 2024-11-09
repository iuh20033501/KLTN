/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.sun.istack.NotNull;

/**
 *
 * @author Windows 10
 */
public class PhoneNumberDTO {
    @NotNull
//    @Pattern(regexp = "^0(?:3[2-9]|8[123458]|7[06789]|5[2689]|9[29])\\d{7}$", message = "Số điện thoại phải là 10 và các đầu số phải thuộc các nhà mạng Viettel (032, 033, 034, 035, 036, 037, 038, 039), Vinaphone (081, 082, 083, 084, 085, 088), MobiFone (070, 076, 077, 078, 079), Vietnamobile (052, 056, 058, 092) và Gmobile (059, 099)")
    private String phone;

    public PhoneNumberDTO() {
    }

    public PhoneNumberDTO(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
}
