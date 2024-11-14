/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.destop.DTO;

import com.mycompany.destop.Enum.SkillEnum;
import com.sun.istack.NotNull;
import java.awt.List;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Windows 10
 */

public class SignupDto {
     private String username;
//    @NotNull(message = "Tên là bắt buộc")
//    @Pattern(regexp = "^([A-ZÀÁẢẠÃĂẰẮẲẶẴÂẦẤẨẬẪĐEÈÉẺẸẼÊỀẾỂỆỄIÌÍỈỊĨOÒÓỎỌÕÔỒỐỔỘỖƠỜỚỞỢỠUÙÚỦỤŨƯỪỨỬỰỮYỲÝỶỴỸa-zàáảạãăằắẳặẵâầấẩậẫđeèéẻẹẽêềếểệễiìíỉịĩoòóỏọõôồốổộỗơờớởợỡuùúủụũưừứửựữyỳýỷỵỹ\\s?])+$", message = "Tên phải có 2 ký tự trở lên")
    private String name;
    private String email;
//    @NotNull(message = "password là bắt buộc")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&+=])(?=\\S+$).{8,32}$", message = "Mật khẩu từ 8 - 32 ký tự gồm tối thiểu 1 chữ cái viết hoa, 1 chữ cái viết thường, 1 chữ số và 1 ký tự đặc biệt")
    private String password;
    private String address;
    private String image;
    private String coverImage;
    private boolean gender;
    @NotNull
    private String phone;
    private LocalDate birthday;
    private String dto;
    private ArrayList<SkillEnum> ListKiNang;
    private Long luong;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getDto() {
        return dto;
    }

    public void setDto(String dto) {
        this.dto = dto;
    }

    public ArrayList<SkillEnum> getListKiNang() {
        return ListKiNang;
    }

    public void setListKiNang(ArrayList<SkillEnum> ListKiNang) {
        this.ListKiNang = ListKiNang;
    }

    public Long getLuong() {
        return luong;
    }

    public void setLuong(Long luong) {
        this.luong = luong;
    }

    public SignupDto() {
    }
    
    
    public SignupDto(String username, String name, String email, String password, String address, String image, boolean gender, String phone, LocalDate birthday, Long luong) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.image = image;
        this.gender = gender;
        this.phone = phone;
        this.birthday = birthday;
        this.luong = luong;
    }



    public SignupDto(String username, String name, String email, String password, String address, String image,  boolean gender, String phone, LocalDate birthday, ArrayList<SkillEnum> listKiNang) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.image = image;
        this.gender = gender;
        this.phone = phone;
        this.birthday = birthday;
        ListKiNang = listKiNang;
    }
}