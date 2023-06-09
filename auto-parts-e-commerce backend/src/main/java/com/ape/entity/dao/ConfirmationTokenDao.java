package com.ape.entity.dao;

import com.ape.entity.concrete.ConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenDao extends JpaRepository<ConfirmationTokenEntity,Long> {
    Optional<ConfirmationTokenEntity> findByToken(String token);
    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationTokenEntity c " +
            "SET c.confirmedAt = :confirmedAt " +
            "WHERE c.token = :token")
    void updateConfirmedAt(@Param("token")String token,
                           @Param("confirmedAt") LocalDateTime confirmedAt);
}
