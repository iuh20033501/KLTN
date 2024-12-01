/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

/**
 *
 * @author Windows 10
 */
public class ChangePassDTO {

    private String newPass;
    private String oldPass;

    public ChangePassDTO(String newPass, String oldPass) {
        this.newPass = newPass;
        this.oldPass = oldPass;
    }

    public ChangePassDTO() {
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

}
