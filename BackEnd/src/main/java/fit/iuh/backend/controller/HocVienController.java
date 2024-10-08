package fit.iuh.backend.controller;

import fit.iuh.backend.service.HocVienService;
import fit.iuh.backend.moudel.HocVien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hocvien")
public class HocVienController {
    @Autowired
    private HocVienService hocVienService;
    @PutMapping("/update/{id}")
    private HocVien updateHocVien( @PathVariable Long id,@RequestBody HocVien hocVien){
        hocVien.setIdUser(id);
        return hocVienService.createHocVien( hocVien);
    }
    @GetMapping("/findbyId/{id}")
    private HocVien findById(@PathVariable Long id){
        return hocVienService.findByIdHocVien(id);
    }

    @GetMapping("/findByName/{name}")
    private HocVien findByName(@PathVariable String name){
        return hocVienService.findByName(name);
    }
    @GetMapping("/findAll")
    private List<HocVien> findAll(){
        return hocVienService.getAll();
    }
}
