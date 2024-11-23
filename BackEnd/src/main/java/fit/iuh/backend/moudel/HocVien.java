package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.Skill;
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
    @ElementCollection(targetClass = Skill.class)
    @Enumerated(EnumType.ORDINAL)
    private List<Skill> kiNangCan;
}
