package com.crking7.datn.repositories;

import com.crking7.datn.models.User;
import com.crking7.datn.models.dtos.TopUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Page<User> findAll(Pageable pageable);

    Optional<User> findById(Long userId);

    User findByUsername(String username);

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% AND (:status IS NULL OR u.status = :status)")
    Page<User> findAllUser(String keyword, Integer status, Pageable pageable);

    @Query("SELECT u.id, u.username, COUNT(o) AS totalQuantity, SUM(oi.sellPrice) AS totalSellPrice " +
            "FROM User u " +
            "JOIN u.orders o " +
            "JOIN o.orderItems oi " +
            "WHERE o.status = 4 " +
            "GROUP BY u.id, u.username " +
            "ORDER BY totalSellPrice DESC " +
            "LIMIT 5")
    List<Object[]> findTopUser();

}
