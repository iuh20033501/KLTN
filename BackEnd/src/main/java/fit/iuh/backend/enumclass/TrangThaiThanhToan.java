package fit.iuh.backend.enumclass;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TrangThaiThanhToan {
    DONE (0),
    CANCEL(1),
    WAIT(2);
    private  int Value;
}
