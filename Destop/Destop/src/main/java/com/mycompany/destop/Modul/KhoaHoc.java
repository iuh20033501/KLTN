/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import com.mycompany.destop.Enum.Skill;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.ArrayList;
import javax.persistence.Column;

/**
 *
 * @author Windows 10
 */
public class KhoaHoc {
    private Long idKhoaHoc;
    private String tenKhoaHoc;
    private Long giaTien;
    private String thoiGianDienRa;
    private Boolean trangThai;
    private Long soBuoi;
    private String moTa;
    @Column(length = 1000000000)
    private String image;
    @ElementCollection(targetClass = Skill.class)
    @Enumerated(EnumType.STRING)
    private ArrayList<Skill> skillEnum;

    public Long getIdKhoaHoc() {
        return idKhoaHoc;
    }

    public void setIdKhoaHoc(Long idKhoaHoc) {
        this.idKhoaHoc = idKhoaHoc;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }

    public Long getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(Long giaTien) {
        this.giaTien = giaTien;
    }

    public String getThoiGianDienRa() {
        return thoiGianDienRa;
    }

    public void setThoiGianDienRa(String thoiGianDienRa) {
        this.thoiGianDienRa = thoiGianDienRa;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public Long getSoBuoi() {
        return soBuoi;
    }

    public void setSoBuoi(Long soBuoi) {
        this.soBuoi = soBuoi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Skill> getSkillEnum() {
        return skillEnum;
    }

    public void setSkillEnum(ArrayList<Skill> skillEnum) {
        this.skillEnum = skillEnum;
    }

    public KhoaHoc(String tenKhoaHoc, Long giaTien, String thoiGianDienRa, Boolean trangThai, Long soBuoi, String moTa, String image, ArrayList<Skill> skillEnum) {
        this.tenKhoaHoc = tenKhoaHoc;
        this.giaTien = giaTien;
        this.thoiGianDienRa = thoiGianDienRa;
        this.trangThai = trangThai;
        this.soBuoi = soBuoi;
        this.moTa = moTa;
        this.image = image;
        this.skillEnum = skillEnum;
    }

    public KhoaHoc() {
    }

    public KhoaHoc(Long idKhoaHoc, String tenKhoaHoc, Long giaTien, String thoiGianDienRa, Boolean trangThai, Long soBuoi, String moTa, String image, ArrayList<Skill> skillEnum) {
        this.idKhoaHoc = idKhoaHoc;
        this.tenKhoaHoc = tenKhoaHoc;
        this.giaTien = giaTien;
        this.thoiGianDienRa = thoiGianDienRa;
        this.trangThai = trangThai;
        this.soBuoi = soBuoi;
        this.moTa = moTa;
        this.image = image;
        this.skillEnum = skillEnum;
    }
    
    
}
