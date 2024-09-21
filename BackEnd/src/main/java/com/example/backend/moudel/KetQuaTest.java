package com.example.backend.moudel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
public class KetQuaTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idKetQua;
    private Long diemTest;
    private Boolean passTest;
    @ManyToOne
    private BaiTest baiTest;
}
