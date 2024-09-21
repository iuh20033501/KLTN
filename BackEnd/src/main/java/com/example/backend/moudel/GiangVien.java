package com.example.backend.moudel;

import com.example.backend.enumclass.SkillEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiangVien extends User{
    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.ORDINAL)
    private List<SkillEnum> chuyenMon;

}
