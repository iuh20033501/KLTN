package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.SkillEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "hocvien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HocVien extends User{

    @ElementCollection(targetClass = SkillEnum.class)
    @Enumerated(EnumType.ORDINAL)
    private List<SkillEnum> kiNangCan;
}