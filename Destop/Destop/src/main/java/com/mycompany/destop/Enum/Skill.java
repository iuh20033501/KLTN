/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Enum;

/**
 *
 * @author Windows 10
 */
public enum Skill {
    LISTEN(0),
    REAL(1),
    WRITE(2),
    SPEAK(3);

    private final int valueSkill;

    // Constructor
    Skill(int valueSkill) {
        this.valueSkill = valueSkill;
    }

    // Getter để lấy giá trị kỹ năng
    public int getValueSkill() {
        return valueSkill;
    }
}

