package com.poa_manager.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.poa_manager.dto.CompanyDTO;
import com.poa_manager.entities.Company;

public class CompanyMapperTest {
  @Test
  void correctlyMapsCompany() {
    Company company = new Company();
    company.setId(1L);
    company.setName("Company");
    company.setType("S.A.");
    CompanyDTO dto = CompanyMapper.toDTO(company);
    assertEquals(company.getId(), dto.getId());
    assertEquals(company.getName(), dto.getName());
    assertEquals(company.getType(), dto.getType());
  }
}
