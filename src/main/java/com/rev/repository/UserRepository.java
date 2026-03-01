package com.rev.repository;

import com.rev.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, Long> {

    // ================= FINDERS =================
    Optional<UserAccount> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UserAccount> findByRole(UserAccount.Role role);

    // ================= CUSTOM UPDATES =================
    @Modifying
    @Transactional
    @Query("UPDATE UserAccount u SET u.status = :status WHERE u.userId = :id")
    int updateStatusById(@Param("id") Long id, @Param("status") String status);

    @Modifying
    @Transactional
    @Query("UPDATE UserAccount u SET u.passwordHash = :password WHERE u.userId = :id")
    int updatePasswordById(@Param("id") Long id, @Param("password") String password);

    // ================= DELETE =================
    @Transactional
    void deleteByUserId(Long id);

    @Transactional
    void deleteByEmail(String email);
}