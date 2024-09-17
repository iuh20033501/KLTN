package com.example.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BuoiHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String maBuoiHoc;
    private String chuDe;
    private Date ngayHoc;
    @ManyToOne
    private LopHoc lopHoc;
}
