package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.CauTraLoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CauTraLoiRepo extends JpaRepository<CauTraLoi,Long> {
    @Query("select c from  CauTraLoi c join CauHoi ch on c.cauHoi.idCauHoi =ch.idCauHoi where c.cauHoi.idCauHoi=:id")
    List<CauTraLoi> findByIdCauHoi(@Param("id") Long id );
}
