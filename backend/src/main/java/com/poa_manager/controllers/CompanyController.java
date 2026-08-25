package com.poa_manager.controllers;

import com.poa_manager.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/companies")
public class CompanyController {
  private final CompanyService companyService;

  @GetMapping("/")
  public ResponseEntity<?> getCompany(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    if (size > 100) {
      return ResponseEntity.badRequest().body("Size must be less than 100");
    }

    return ResponseEntity.ok(companyService.getCompanies(page, size));
  }

  @GetMapping("/{id}/members")
  public ResponseEntity<?> getMembers(@PathVariable Long id, @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    if (size > 100) {
      return ResponseEntity.badRequest().body("Size must be less than 100");
    }
    return ResponseEntity.ok(companyService.getMembers(id, page, size));
  }
}
