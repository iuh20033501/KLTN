/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import com.mycompany.destop.Enum.SkillEnum;
import java.awt.List;

/**
 *
 * @author Windows 10
 */

public class HocVien extends User {
    @ElementCollectio(targetClass = SkillEnum.class)
    @Enumerated(EnumTyp.ORDINAL)
    private List<SkillEnum> kiNangCan;

    // Getter và setter cho kiNangCan
    public List<SkillEnum> getKiNangCan() {
        return kiNangCan;
    }

    public void setKiNangCa(List<SkillEnum> kiNangCan) {
        this.kiNangCan = kiNangCan;
    }
}

