package com.poa_manager.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.poa_manager.entities.Company;
import com.poa_manager.entities.Person;
import com.poa_manager.repositories.CompanyRepository;
import com.poa_manager.repositories.PersonRepository;
import com.poa_manager.responses.CompaniesResponse;
import com.poa_manager.responses.MembersResponse;

public class CompanyServiceTest {
  private CompanyRepository companyRepository;
  private PersonRepository personRepository;
  private CompanyService companyService;

  private Company company;
  private Person alice;

  @BeforeEach
  void setUp() {
    company = new Company();
    company.setId(1L);
    company.setName("Company Test");

    alice = new Person();
    alice.setId(1L);
    alice.setFirstName("Alice");
    alice.setLastName("Doe");

    companyRepository = Mockito.mock(CompanyRepository.class);
    personRepository = Mockito.mock(PersonRepository.class);
    companyService = new CompanyService(companyRepository, personRepository);
  }

  @Test
  void returnsCompanies() {
    Mockito.when(companyRepository.findAll(ArgumentMatchers.any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(company), PageRequest.of(0, 10), 1));
    CompaniesResponse response = companyService.getCompanies(0, 10);
    assertEquals(1, response.getCompanies().size());
    assertEquals(1L, response.getCompanies().get(0).getId());
    assertEquals("Company Test", response.getCompanies().get(0).getName());
  }

  @Test
  void returnMembers() {
    Mockito.when(personRepository.findByCompanies_Company_Id(
        ArgumentMatchers.eq(1L), ArgumentMatchers.any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(alice), PageRequest.of(0, 10), 1));
    MembersResponse response = companyService.getMembers(1L, 0, 10);
    assertEquals(1, response.getMembers().size());
    assertEquals("Alice", response.getMembers().get(0).getFirstName());
    assertEquals("Doe", response.getMembers().get(0).getLastName());
    assertEquals(1L, response.getCount());
  }

}
