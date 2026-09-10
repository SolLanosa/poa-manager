package com.poa_manager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.poa_manager.controllers.CompanyController;
import com.poa_manager.dto.CompanyDTO;
import com.poa_manager.responses.CompaniesResponse;
import com.poa_manager.responses.MembersResponse;
import com.poa_manager.services.CompanyService;

public class CompanyControllerTest {
  private CompanyService companyService;
  private CompanyController companyController;

  @BeforeEach
  void setUp() {
    companyService = Mockito.mock(CompanyService.class);
    companyController = new CompanyController(companyService);
  }

  @Test
  void returnOkCompanies() {
    CompaniesResponse companies = new CompaniesResponse(List.of(new CompanyDTO(1L, "Company", "SA")), 1L);
    Mockito.when(companyService.getCompanies(0, 10)).thenReturn(companies);
    ResponseEntity<?> response = companyController.getCompany(0, 10);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(companies, response.getBody());
  }

  @Test
  void returnCompanyMember() {
    MembersResponse members = new MembersResponse(List.of(), 1L);
    Mockito.when(companyService.getMembers(1L, 0, 10)).thenReturn(members);
    ResponseEntity<?> response = companyController.getMembers(1L, 0, 10);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(members, response.getBody());
  }

}
