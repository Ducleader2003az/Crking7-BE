package com.crking7.datn.web;

import com.crking7.datn.services.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
public class CompanyRest {
    private final CompanyService companyService;
    public CompanyRest(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyInfo(@PathVariable("id") long companyId){
        try{
            return ResponseEntity.ok(companyService.getCompany(companyId));
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
}
