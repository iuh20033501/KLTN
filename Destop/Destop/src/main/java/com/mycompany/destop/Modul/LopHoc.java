/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.Modul;

import com.mycompany.destop.Enum.LopEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import java.util.Date;

/**
 *
 * @author Windows 10
 */
public class LopHoc {
     private  Long idLopHoc;
    private Long soHocVien;
    private  String tenLopHoc;
    private LopEnum trangThai;
    private Date ngayBD;
    private Date ngayKT;
    @ManyToOne
    private GiangVien giangVien;
    @ManyToOne(cascade = CascadeType.ALL)
    private KhoaHoc khoaHoc;
//    @OneToMany(mappedBy = "HocVienLopHocKey.lopHoc")
//    private List<HocVienLopHoc> hocVienLopHocs;
    private  String moTa;

    public Long getIdLopHoc() {
        return idLopHoc;
    }

    public void setIdLopHoc(Long idLopHoc) {
        this.idLopHoc = idLopHoc;
    }

    public Long getSoHocVien() {
        return soHocVien;
    }

    public void setSoHocVien(Long soHocVien) {
        this.soHocVien = soHocVien;
    }

    public String getTenLopHoc() {
        return tenLopHoc;
    }

    public void setTenLopHoc(String tenLopHoc) {
        this.tenLopHoc = tenLopHoc;
    }

    public LopEnum getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(LopEnum trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayBD() {
        return ngayBD;
    }

    public void setNgayBD(Date ngayBD) {
        this.ngayBD = ngayBD;
    }

    public Date getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(Date ngayKT) {
        this.ngayKT = ngayKT;
    }

    public GiangVien getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(GiangVien giangVien) {
        this.giangVien = giangVien;
    }

    public KhoaHoc getKhoaHoc() {
        return khoaHoc;
    }

    public void setKhoaHoc(KhoaHoc khoaHoc) {
        this.khoaHoc = khoaHoc;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LopHoc(Long soHocVien, String tenLopHoc, LopEnum trangThai, Date ngayBD, Date ngayKT, GiangVien giangVien, KhoaHoc khoaHoc, String moTa) {
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
        this.trangThai = trangThai;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.giangVien = giangVien;
        this.khoaHoc = khoaHoc;
        this.moTa = moTa;
    }

    public LopHoc() {
    }

    public LopHoc(Long idLopHoc, Long soHocVien, String tenLopHoc, LopEnum trangThai, Date ngayBD, Date ngayKT, GiangVien giangVien, KhoaHoc khoaHoc, String moTa) {
        this.idLopHoc = idLopHoc;
        this.soHocVien = soHocVien;
        this.tenLopHoc = tenLopHoc;
        this.trangThai = trangThai;
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.giangVien = giangVien;
        this.khoaHoc = khoaHoc;
        this.moTa = moTa;
    }
    
    
}