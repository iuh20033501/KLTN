package fit.iuh.backend.controller;

import fit.iuh.backend.dto.UpdateUserDTO;
import fit.iuh.backend.moudel.GiangVien;
import fit.iuh.backend.moudel.NhanVien;
import fit.iuh.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/nhanVien")
public class NhanVienController {
    @Autowired
    private NhanVienService nhanVienService;
    @Qualifier("authServiceImpl")
    @Autowired
    private AuthService service;

    @Qualifier("taiKhoanImplement")
    @Autowired
    private TaiKhoanService tkService;

    @Autowired
    private UserService userService;
    @Autowired
    private LopHocService lopHocService;

    @PutMapping("/update/{id}")
    public NhanVien updateHocVien(@PathVariable Long id, @RequestBody NhanVien nhanVien){
        NhanVien f=nhanVienService.findById(id).orElseThrow(()->new RuntimeException("nhan vien not found "));
        f.setEmail(nhanVien.getEmail());
        f.setDiaChi(nhanVien.getDiaChi());
        f.setImage(nhanVien.getImage());
        f.setSdt(nhanVien.getSdt());
        f.setGioiTinh(nhanVien.isGioiTinh());
        f.setHoTen(nhanVien.getHoTen());
        f.setNgaySinh(nhanVien.getNgaySinh());
//        f.setLuongThang(giangVien.getLuong());
        return nhanVienService.createNhanVien(f);
    }
    //
//    @PutMapping("/updateUser/{id}")
//    public NhanVien updateHocVien(@PathVariable Long id, @RequestBody UpdateUserDTO dto){
//        NhanVien f=nhanVienService.findById(id).orElseThrow(()->new RuntimeException("nhan vien not found "));
//        f.setEmail(dto.getNhanVien().getEmail());
//        f.setDiaChi(dto.getNhanVien().getDiaChi());
//        f.setImage(dto.getNhanVien().getImage());
//        f.setSdt(dto.getNhanVien().getSdt());
//        f.setGioiTinh(dto.getNhanVien().isGioiTinh());
//        f.setHoTen(dto.getNhanVien().getHoTen());
//        f.setNgaySinh(LocalDate.parse(dto.getGson()));
////        f.setLuongThang(giangVien.getLuong());
//        return nhanVienService.createNhanVien(f);
//    }
    @GetMapping("/findbyId/{id}")
    public NhanVien findById(@PathVariable Long id){
        return nhanVienService.findById(id).orElse(null);
    }

    @GetMapping("/findByName/{name}")
    public NhanVien findByName(@PathVariable String name){
        return nhanVienService.findByName(name);
    }
    @GetMapping("/findAll")
    public List<NhanVien> findAll(){
        return nhanVienService.findAlL();
    }
}
