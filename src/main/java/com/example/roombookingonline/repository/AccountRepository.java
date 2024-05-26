package com.example.roombookingonline.repository;

import com.example.roombookingonline.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    @Query(value = "from AccountEntity acc " +
            "where acc.username=?1")
    Optional<AccountEntity> findByUsername(String username);
}
