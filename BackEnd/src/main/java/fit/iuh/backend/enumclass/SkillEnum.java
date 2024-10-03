package fit.iuh.backend.enumclass;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SkillEnum {
    LISTEN (0),
    REAL(1),
    WRITE(2),
    SPEAK(3);

    private final int ValueSkill;
}
