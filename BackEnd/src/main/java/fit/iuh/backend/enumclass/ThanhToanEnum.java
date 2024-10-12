package fit.iuh.backend.enumclass;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ThanhToanEnum {
    DONE (0),
    CANCEL(1),
    WAIT(2);
    private  int Value;
}
