package com.poa_manager.controllers;

import com.poa_manager.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/")
    public ResponseEntity<?> getCompany(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        if (size > 100) {
            return ResponseEntity.badRequest().body("Size must be less than 100");
        }

        return ResponseEntity.ok(companyService.getCompanies(page, size));
    }
}
