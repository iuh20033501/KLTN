/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import com.mycompany.destop.Enum.Skill;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Windows 10
 */
public class GiangVien extends User{
    @ElementCollection(targetClass = Skill.class)
    @Enumerated(EnumType.ORDINAL)
    private ArrayList<Skill> chuyenMon;
    private Long luong;

    public ArrayList<Skill> getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(ArrayList<Skill> chuyenMon) {
        this.chuyenMon = chuyenMon;
    }

    public Long getLuong() {
        return luong;
    }

    public void setLuong(Long luong) {
        this.luong = luong;
    }

    public GiangVien(ArrayList<Skill> chuyenMon, Long luong) {
        this.chuyenMon = chuyenMon;
        this.luong = luong;
    }

    public GiangVien(ArrayList<Skill> chuyenMon, Long luong, String hoTen, String sdt, String diaChi, String email, LocalDate ngaySinh, boolean gioiTinh, String image) {
        super(hoTen, sdt, diaChi, email, ngaySinh, gioiTinh, image);
        this.chuyenMon = chuyenMon;
        this.luong = luong;
    }

    public GiangVien() {
    }
    
}
