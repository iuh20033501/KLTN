package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.BuoiHoc;
import fit.iuh.backend.service.BuoiHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/buọihoc")
public class BuoiHocController {
    @Autowired
    private BuoiHocService buoiHocService;
    @PostMapping("/createBuoiHoc")
    private BuoiHoc createBuoiHoc(@RequestBody BuoiHoc buoiHoc){
        return buoiHocService.createBuoiHoc(buoiHoc);
    }
    @GetMapping("/getbuoiHocByLop/{idLop}")
    private List<BuoiHoc> getBuoiByLop(@PathVariable  Long idLop){
        return buoiHocService.getByIdLop(idLop);
    }
    @GetMapping("/getAll")
    private List<BuoiHoc> getAll(){
        return buoiHocService.getAll();
    }
    @PostMapping("/update/{id}")
    private BuoiHoc updateBuoiHoc(@PathVariable Long id ,@RequestBody BuoiHoc buoiHoc){
        buoiHoc.setIdBuoiHoc(id);
        return buoiHocService.createBuoiHoc(buoiHoc);
    }
    @GetMapping("/getByHocVien/{id}")
    private List<BuoiHoc> getByHocVien(@PathVariable Long id ){
        return buoiHocService.getBuoiByHocVien(id);
    }

}
