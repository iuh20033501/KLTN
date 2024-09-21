package com.example.backend.Repository;

import com.example.backend.moudel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository <User,Long>{
}
