package com.example.backend.moudel;

import com.example.backend.enumclass.TestEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idTest;
    private Date ngayBD;
    private Date ngayKT;
    private Time thoiGianLamBai;
    @ManyToOne
    private LopHoc lopHoc;
    private TestEnum loaiTest;
}
