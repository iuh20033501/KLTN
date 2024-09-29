package com.example.backend.controller;

import com.example.backend.Service.*;
import com.example.backend.dto.Logindto;
import com.example.backend.dto.SignupGiangViendto;
import com.example.backend.dto.SignupHocViendto;
import com.example.backend.dto.SignupNhanViendto;
import com.example.backend.moudel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class TaiKhoanController {
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private UserService userService;
    @Autowired
    private HocVienService hocVienService;
    @Autowired
    private GiangVienService giangVienService;
    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/findByName/{id}")
    public TaiKhoan getByName(@PathVariable String tenTaiKhoan){
        return  taiKhoanService.findByTenDangNhap(tenTaiKhoan);
    }
    @PutMapping("/update")
    public TaiKhoan updateTaiKhoan(@RequestBody TaiKhoan taiKhoan){
        if (taiKhoanService.findByTenDangNhap(taiKhoan.getTenDangNhap())!=null)
        return taiKhoanService.createTaiKhoan(taiKhoan);
        return null;
    }
    @PostMapping("/login/{id}")
    public Logindto Login(@PathVariable String tenTaiKhoan, @RequestParam String matKhau) {
        TaiKhoan taiKhoan = taiKhoanService.findByTenDangNhap(tenTaiKhoan);
        if (taiKhoan.getTenDangNhap() != null) {
            if (taiKhoan.getMatKhau().equals(matKhau)) {
                if (taiKhoan.getTrangThai().equals(true))
                    return new Logindto(userService.getRole(taiKhoan.getUser().getIdUser()),taiKhoan.getUser());
                else return new Logindto("tài khoản bị khóa",taiKhoan.getUser());
            } else return new Logindto("sai mật khẩu",null);
        }
        return new Logindto("không tìm thấy tài khoản",null);
    }
    @PostMapping("/singup/hocvien")
    public HocVien Signup(@RequestBody SignupHocViendto signupHocViendto){
        HocVien hocVien = signupHocViendto.getHocVien();
        TaiKhoan taiKhoan = signupHocViendto.getTaiKhoan();
       HocVien hv = hocVienService.createHocVien(hocVien);
        if(hv!=null){
            if(taiKhoanService.createTaiKhoan(taiKhoan)!=null){
                return hv;
            }
        }
        return null;
    }
    @PostMapping("/singup/giangvien")
    public GiangVien Signup(@RequestBody SignupGiangViendto signupGiangViendto){
        GiangVien giangVien = signupGiangViendto.getGiangVien();
        TaiKhoan taiKhoan = signupGiangViendto.getTaiKhoan();
        GiangVien gv = giangVienService.createGiangVien(giangVien);
        if(gv!=null){
            if(taiKhoanService.createTaiKhoan(taiKhoan)!=null){
                return gv;
            }
        }
        return null;
    }
    @PostMapping("/singup/nhanvien")
    public NhanVien Signup(@RequestBody SignupNhanViendto signupNhanViendto){
        NhanVien nhanVien = signupNhanViendto.getNhanVien();
        TaiKhoan taiKhoan = signupNhanViendto.getTaiKhoan();
        NhanVien nv = nhanVienService.createNhanVien(nhanVien);
        if(nv!=null){
            if(taiKhoanService.createTaiKhoan(taiKhoan)!=null){
                return nv;
            }
        }
        return null;
    }
}
