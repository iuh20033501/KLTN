/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

/**
 *
 * @author Windows 10
 */
public class ResetPassDto {
     private String password;

    public ResetPassDto(String password) {
        this.password = password;
    }

    public ResetPassDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
