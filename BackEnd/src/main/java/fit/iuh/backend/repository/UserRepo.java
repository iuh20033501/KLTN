package fit.iuh.backend.repository;

import fit.iuh.backend.moudel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository <User,Long>{
    Optional<User> findByHoTen(String hoTen);
//    Optional<User> findBySdt(String sdt);
}
