/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import com.mycompany.destop.Enum.Skill;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author Windows 10
 */

public class HocVien extends User{
    @ElementCollection(targetClass = Skill.class)
    @Enumerated(EnumType.ORDINAL)
    private ArrayList<Skill> kiNangCan;

    // Constructor
    public HocVien() {
        kiNangCan = new ArrayList<>(); // Khởi tạo danh sách
    }

    // Getter cho kiNangCan
    public ArrayList<Skill> getKiNangCan() {
        return kiNangCan;
    }

    // Setter cho kiNangCan
    public void setKiNangCan(ArrayList<Skill> kiNangCan) {
        this.kiNangCan = kiNangCan;
    }

    // Phương thức thêm kỹ năng
    public void addSkill(Skill skill) {
        kiNangCan.add(skill);
    }
}

