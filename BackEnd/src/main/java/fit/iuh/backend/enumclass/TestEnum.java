package fit.iuh.backend.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TestEnum {
    GK(0),
    CK(1);


    private final int ValueTest;
}
