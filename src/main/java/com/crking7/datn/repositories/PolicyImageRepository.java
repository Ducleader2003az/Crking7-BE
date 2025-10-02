package com.crking7.datn.repositories;

import com.crking7.datn.models.PolicyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyImageRepository extends JpaRepository<PolicyImage, Long> {
	List<PolicyImage> findByPolicyId(long policyId);
	
	List<PolicyImage> findByStatus(int status);

}
