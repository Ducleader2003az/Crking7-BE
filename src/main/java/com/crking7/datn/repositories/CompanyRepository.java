package com.crking7.datn.repositories;

import com.crking7.datn.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByIdAndStatus(long companyId, int status);
}
