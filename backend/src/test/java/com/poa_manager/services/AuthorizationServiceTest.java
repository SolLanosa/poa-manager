package com.poa_manager.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.poa_manager.dto.AuthorizationRequestDTO;
import com.poa_manager.entities.Company;
import com.poa_manager.entities.Decision;
import com.poa_manager.entities.FacultyAction;
import com.poa_manager.entities.FacultyObject;
import com.poa_manager.entities.FacultyScopeItem;
import com.poa_manager.entities.Person;
import com.poa_manager.entities.PowerOfAttorney;
import com.poa_manager.entities.PowerOfAttorneyFaculty;
import com.poa_manager.entities.PowerOfAttorneyGroup;
import com.poa_manager.entities.PowerOfAttorneyGroupMember;
import com.poa_manager.entities.PowerOfAttorneySigningRule;
import com.poa_manager.entities.PowerOfAttorneySigningRuleRequirement;
import com.poa_manager.repositories.CompanyRepository;
import com.poa_manager.repositories.PersonRepository;
import com.poa_manager.repositories.PowerOfAttorneyRepository;

public class AuthorizationServiceTest {
  private AuthorizationService authorizationService;
  private PowerOfAttorneyRepository powerOfAttorneyRepository;

  private Company company;
  private Person alice;
  private Person bob;
  private PowerOfAttorney powerOfAttorney;
  private PowerOfAttorneyFaculty faculty;
  private PowerOfAttorneyGroup groupA;
  private PowerOfAttorneySigningRule signingRule;

  @BeforeEach
  void setUp() {

    company = new Company();
    company.setId(1L);
    company.setName("Company Test");

    alice = new Person();
    alice.setId(1L);
    alice.setFirstName("Alice");
    alice.setLastName("Doe");

    bob = new Person();
    bob.setId(2L);
    bob.setFirstName("Bob");
    bob.setLastName("Doe");

    powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setId(1L);
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setValidFrom(LocalDate.of(2025, 1, 1).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2026, 12, 31).atStartOfDay());
    powerOfAttorney.setPowerOfAttorneyGroups(new ArrayList<>());
    powerOfAttorney.setFaculties(new ArrayList<>());

    groupA = new PowerOfAttorneyGroup();
    groupA.setId(1L);
    groupA.setPowerOfAttorney(powerOfAttorney);
    groupA.setLabel("Group A");
    groupA.setMembers(new ArrayList<>());
    groupA.getMembers().add(createMember(groupA, alice));
    groupA.getMembers().add(createMember(groupA, bob));
    powerOfAttorney.getPowerOfAttorneyGroups().add(groupA);

    faculty = new PowerOfAttorneyFaculty();
    faculty.setId(1L);
    faculty.setPowerOfAttorney(powerOfAttorney);
    faculty.setAction(FacultyAction.PAY);
    faculty.setObjectCategory(FacultyObject.TAX_OBLIGATION);
    FacultyScopeItem scopeItem = new FacultyScopeItem();
    scopeItem.setExternalRef("tax-1");
    scopeItem.setFaculty(faculty);
    scopeItem.setId(1L);
    faculty.setScopeItems(List.of(scopeItem));
    faculty.setSigningRules(new ArrayList<>());
    powerOfAttorney.getFaculties().add(faculty);

    signingRule = new PowerOfAttorneySigningRule();
    signingRule.setId(1L);
    signingRule.setPowerOfAttorneyFaculty(faculty);
    signingRule.setMaxAmount(new BigDecimal("300000.00"));
    signingRule.setCurrency("USD");
    signingRule.setPowerOfAttorneySigningRuleRequirements(new ArrayList<>());
    faculty.getSigningRules().add(signingRule);

    PowerOfAttorneySigningRuleRequirement ruleRequirement = new PowerOfAttorneySigningRuleRequirement();
    ruleRequirement.setPowerOfAttorneySigningRule(signingRule);
    ruleRequirement.setPowerOfAttorneyGroup(groupA);
    ruleRequirement.setCountRequired(2);
    signingRule.getPowerOfAttorneySigningRuleRequirements().add(ruleRequirement);

    CompanyRepository companyRepository = Mockito.mock(CompanyRepository.class);
    PersonRepository personRepository = Mockito.mock(PersonRepository.class);
    powerOfAttorneyRepository = Mockito.mock(PowerOfAttorneyRepository.class);

    Mockito.when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
    Mockito.when(personRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(alice, bob));
    Mockito.when(personRepository.findAllById(List.of(1L))).thenReturn(List.of(alice));
    Mockito.when(powerOfAttorneyRepository.findByCompany_Id(1L)).thenReturn(List.of(powerOfAttorney));

    authorizationService = new AuthorizationService(companyRepository, personRepository,
        powerOfAttorneyRepository);
  }

  @Test
  void approvesWhenHasTwoSignersFromGroupA() {
    AuthorizationRequestDTO request = getAuthorizationRequest();

    Decision decision = authorizationService.authorize(request);

    assertTrue(decision.isApproved());
    assertEquals(Optional.of(1L), decision.getPowerOfAttorneyId());
    assertEquals(Optional.of(1L), decision.getFacultyId());
    assertEquals(Optional.of(1L), decision.getSigningRuleId());
    assertEquals(1, decision.getObservations().size());
    assertEquals("APPROVED", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenHasOnlyOneSignerFromGroupA() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setSignersId(List.of(1L));
    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("INSUFFICIENT_SIGNATURES", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenDifferentAction() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setAction(FacultyAction.CLOSE);
    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("NO_FACULTY", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenDifferentObject() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setObject(FacultyObject.ACCOUNTING);
    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("NO_FACULTY", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenOutsideValidityDate() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setDate(LocalDate.of(2028, 1, 1).atStartOfDay());
    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("NO_POA_IN_FORCE", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenSignersButAboveAmount() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setAmount(new BigDecimal(9999999999.00));

    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("AMOUNT_OUT_OF_BAND", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenFacultyHasNoSigningRule() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    faculty.setSigningRules(new ArrayList<>());
    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("AMOUNT_OUT_OF_BAND", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenNoItemRef() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setItemRef(null);

    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("OUT_OF_SCOPE", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenDifferentItemRef() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setItemRef("other");

    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("OUT_OF_SCOPE", decision.getObservations().get(0).code());
  }

  @Test
  void rejectsWhenNoPoa() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    Mockito.when(powerOfAttorneyRepository.findByCompany_Id(1L)).thenReturn(List.of());
    Decision decision = authorizationService.authorize(request);
    assertFalse(decision.isApproved());
    assertEquals(1, decision.getObservations().size());
    assertEquals("NO_POA_IN_FORCE", decision.getObservations().get(0).code());
  }

  @Test
  void exceptionWhenCompanyDoesNotExist() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setCompanyId(11L);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> authorizationService.authorize(request));
    assertEquals("Unknown company 11", exception.getMessage());
  }

  @Test
  void exceptionWhenSignerDoesNotExist() {
    AuthorizationRequestDTO request = getAuthorizationRequest();
    request.setSignersId(List.of(11L));
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> authorizationService.authorize(request));
    assertEquals("One or more signer ids do not exist", exception.getMessage());
  }

  private AuthorizationRequestDTO getAuthorizationRequest() {
    AuthorizationRequestDTO request = new AuthorizationRequestDTO();
    request.setCompanyId(1L);
    request.setSignersId(List.of(1L, 2L));
    request.setAction(FacultyAction.PAY);
    request.setObject(FacultyObject.TAX_OBLIGATION);
    request.setItemRef("tax-1");
    request.setAmount(new BigDecimal(1000.00));
    request.setCurrency("USD");
    request.setDate(LocalDate.of(2025, 6, 1).atStartOfDay());
    return request;
  }

  private PowerOfAttorneyGroupMember createMember(PowerOfAttorneyGroup group, Person person) {
    PowerOfAttorneyGroupMember member = new PowerOfAttorneyGroupMember();
    member.setPowerOfAttorneyGroup(group);
    member.setPerson(person);
    return member;
  }

}
