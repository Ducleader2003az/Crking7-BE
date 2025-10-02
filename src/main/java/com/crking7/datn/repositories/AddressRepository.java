package com.crking7.datn.repositories;

import com.crking7.datn.models.Address;
import com.crking7.datn.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(long userId);

}
