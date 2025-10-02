package com.crking7.datn.web.admin;

import com.crking7.datn.services.CompanyService;
import com.crking7.datn.web.dto.request.CompanyRequest;
import com.crking7.datn.web.dto.response.CompanyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/company")
public class ACompanyRest {
    private final CompanyService companyService;

    public ACompanyRest(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCompany(@RequestBody CompanyRequest companyRequest){
        try{
            CompanyResponse companyResponse = companyService.createCompanyInfo(companyRequest);
            return ResponseEntity.ok(companyResponse);
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable("id") long companyId,
                                           @RequestBody CompanyRequest companyRequest){
        try{
            CompanyResponse companyResponse = companyService.updateCompanyInfo(companyId, companyRequest);
            return ResponseEntity.ok(companyResponse);
        }catch (Exception e){
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
}
