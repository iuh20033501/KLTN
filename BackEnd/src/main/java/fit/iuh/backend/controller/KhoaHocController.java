package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.KhoaHoc;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.service.KhoaHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/khoahoc")
public class KhoaHocController {
    @Autowired
    private KhoaHocService khoaHocService;

    @PostMapping("/create")
    public KhoaHoc createLop ( @RequestBody KhoaHoc khoa){
//        khoa.setTrangThai(true);
        System.out.println("anh"+khoa.getSkill().toString());
        return khoaHocService.createKhoaHoc(khoa);
    }
    //
    @GetMapping("/findKhoa/{id}")
    public ResponseEntity<KhoaHoc> findByidKhoa(@PathVariable("id") Long idKhoa) {
        Optional<KhoaHoc> khoaHoc = khoaHocService.findById(idKhoa);
        if (khoaHoc.isPresent()) {
            return ResponseEntity.ok(khoaHoc.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Trả về 404 nếu không tìm thấy
    }
    //
    @GetMapping("/xoaKhoa/{idKhoa}")
    public KhoaHoc deleteKhoa (@PathVariable Long idKhoa){
        KhoaHoc khoaHoc = khoaHocService.findById(idKhoa).orElseThrow(() -> new RuntimeException("Khoa hoc not found"));
        khoaHoc.setTrangThai(false);
        return khoaHocService.createKhoaHoc(khoaHoc);
    }
    @GetMapping("/getAll")
    public List<KhoaHoc> findAll (){
        return  khoaHocService.getAll();
    }
    @GetMapping("/getAllTrue")
    public List<KhoaHoc> findAllTrue (){
        return  khoaHocService.getAllTrue();
    }

    @GetMapping("/getListActiveTrueLikeName/{name}")
    public List<KhoaHoc> findActiveTrueLikeName(@PathVariable("name") String name){
        return  khoaHocService.getListKhoaActiveTrueLikeName(name);
    }
    @GetMapping("/getListLikeName/{name}")
    public List<KhoaHoc> findActiveLikeName(@PathVariable("name") String name){
        return  khoaHocService.getListKhoaLikeName(name);
    }
    @GetMapping("/getListInYear/{year}")
    public List<KhoaHoc> findInYear(@PathVariable("year") String year){
        return  khoaHocService.getListKhoaYear(year);
    }





}
