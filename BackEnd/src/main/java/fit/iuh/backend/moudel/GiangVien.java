package fit.iuh.backend.moudel;

import fit.iuh.backend.enumclass.Skill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "giang_vien")
public class GiangVien extends User{
    @ElementCollection(targetClass = Skill.class)
    @Enumerated(EnumType.ORDINAL)
    private List<Skill> chuyenMon;
    private Long luong;

}
