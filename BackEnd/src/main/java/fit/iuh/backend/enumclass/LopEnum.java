package fit.iuh.backend.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LopEnum {
    READY (0),
    DELETE(1),
    FULL(2);

    private final int ValueLop;
}
