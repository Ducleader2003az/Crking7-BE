package com.crking7.datn.repositories;

import com.crking7.datn.models.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PolicyRepository extends JpaRepository<Policy, Long> {
	List<Policy> findByCategoryPolicyId(long categoryPolicyId);

	List<Policy> findByStatus(int status);
}
