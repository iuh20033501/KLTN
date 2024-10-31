package fit.iuh.backend.controller;

import fit.iuh.backend.moudel.BuoiHoc;
import fit.iuh.backend.moudel.LopHoc;
import fit.iuh.backend.service.BuoiHocService;
import fit.iuh.backend.service.LopHocService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buoihoc")
public class BuoiHocController {
    @Autowired
    private BuoiHocService buoiHocService;
    @Autowired
    private LopHocService lopHocService;
    @Operation(
            summary = "Tạo Buổi học",
            description = """ 
            truyền idlop vao parm
            truyền đày đủ ngày học , giờ học, giờ kết thúc, noi học, chủ dề, và trang thái họcOnl;
    """
    )
    @PostMapping("/createBuoiHoc/{idLop}")
    public ResponseEntity<BuoiHoc> createBuoiHoc(@PathVariable Long idLop, @RequestBody BuoiHoc buoiHoc){
      LopHoc lop= lopHocService.findById(idLop).orElseThrow(() -> new RuntimeException("Buoi hoc not found"));
      buoiHoc.setLopHoc(lop);
        return ResponseEntity.status(HttpStatus.CREATED).body(buoiHocService.createBuoiHoc(buoiHoc));
    }
    @GetMapping("/getbuoiHocByLop/{idLop}")
    public List<BuoiHoc> getBuoiByLop(@PathVariable  Long idLop){
        return buoiHocService.getByIdLop(idLop);
    }
    @GetMapping("/getAll")
    public List<BuoiHoc> getAll(){
        return buoiHocService.getAll();
    }
    @Operation(
            summary = "Update Buổi học",
            description = """ 
            truyền idlop, idBuoi vao parm
            truyền đày đủ ngày học , giờ học, giờ kết thúc, noi học, chủ dề, và trang thái họcOnl;
    """
    )
    @PutMapping("/update/{idLop}/{idBuoi}")
    public ResponseEntity<BuoiHoc> updateBuoiHoc(@PathVariable Long idLop,@PathVariable Long idBuoi ,@RequestBody BuoiHoc buoiHoc){
        LopHoc lop= lopHocService.findById(idLop).orElseThrow(() -> new RuntimeException("Buoi hoc not found"));
        buoiHoc.setLopHoc(lop);
        buoiHoc.setIdBuoiHoc(idBuoi);
        return ResponseEntity.status(HttpStatus.CREATED).body(buoiHocService.createBuoiHoc(buoiHoc));
    }
    @GetMapping("/getByHocVien/{id}")
    public List<BuoiHoc> getByHocVien(@PathVariable Long id ){
        return buoiHocService.getBuoiByHocVien(id);
    }

}
