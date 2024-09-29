package com.example.backend.moudel;

import com.example.backend.enumclass.ChucVuEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien extends User{

    @Enumerated(EnumType.ORDINAL)
    private ChucVuEnum chucVu;

}
