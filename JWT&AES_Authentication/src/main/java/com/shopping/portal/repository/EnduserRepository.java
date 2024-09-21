package com.shopping.portal.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.shopping.portal.model.EndUser;
import jakarta.transaction.Transactional;

public interface EnduserRepository  extends JpaRepository<EndUser,Long>{
    Optional<EndUser> findByEmail(String email);
    
}
