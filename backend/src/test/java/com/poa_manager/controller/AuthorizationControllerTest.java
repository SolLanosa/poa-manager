package com.poa_manager.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.poa_manager.controllers.AuthorizationController;
import com.poa_manager.dto.AuthorizationRequestDTO;
import com.poa_manager.dto.DecisionDTO;
import com.poa_manager.entities.Decision;
import com.poa_manager.entities.FacultyAction;
import com.poa_manager.entities.FacultyObject;
import com.poa_manager.services.AuthorizationService;

public class AuthorizationControllerTest {
  private AuthorizationService authorizationService;
  private AuthorizationController authorizationController;

  @BeforeEach
  void setUp() {
    authorizationService = Mockito.mock(AuthorizationService.class);
    authorizationController = new AuthorizationController(authorizationService);
  }

  @Test
  void failsWhenMissingCompanyId() {
    AuthorizationRequestDTO request = getRequest();
    request.setCompanyId(null);
    ResponseEntity<?> response = authorizationController.authorize(request);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("companyId, action and object are required", response.getBody());
  }

  @Test
  void failsWhenMissingAction() {
    AuthorizationRequestDTO request = getRequest();
    request.setAction(null);
    ResponseEntity<?> response = authorizationController.authorize(request);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("companyId, action and object are required", response.getBody());
  }

  @Test
  void failsWhenMissingObject() {
    AuthorizationRequestDTO request = getRequest();
    request.setObject(null);
    ResponseEntity<?> response = authorizationController.authorize(request);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("companyId, action and object are required", response.getBody());
  }

  @Test
  void failsWhenSignerIdIsMissing() {
    AuthorizationRequestDTO request = getRequest();
    request.setSignersId(null);
    ResponseEntity<?> response = authorizationController.authorize(request);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("One signer is required", response.getBody());
  }

  @Test
  void failsWhenSignerIdIsEmptyList() {
    AuthorizationRequestDTO request = getRequest();
    request.setSignersId(List.of());
    ResponseEntity<?> response = authorizationController.authorize(request);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("One signer is required", response.getBody());
  }

  @Test
  void isResponseOkWithApprovedDecision() {
    Decision decision = new Decision();
    decision.approve("Banking", "Faculty", "Rule");
    decision.addObservation("APPROVED", "");

    Mockito.when(authorizationService.authorize(ArgumentMatchers.any(AuthorizationRequestDTO.class)))
        .thenReturn(decision);

    ResponseEntity<?> response = authorizationController.authorize(getRequest());

    assertEquals(HttpStatus.OK, response.getStatusCode());

    DecisionDTO body = (DecisionDTO) response.getBody();
    assertTrue(body.isApproved());
    assertEquals("Banking", body.getPowerOfAttorney());
    assertEquals("Faculty", body.getFaculty());
    assertEquals("Rule", body.getSigningRule());
    assertEquals(1, body.getObservations().size());
    assertEquals("APPROVED", body.getObservations().get(0).getCode());
  }

  @Test
  void isResponseOkWithRejectedDecision() {
    Decision decision = new Decision();
    decision.addObservation("NO_FACULTY",
        "No power of attorney in force grants PAY on TAX_OBLIGATION");

    Mockito.when(authorizationService.authorize(ArgumentMatchers.any(AuthorizationRequestDTO.class)))
        .thenReturn(decision);

    ResponseEntity<?> response = authorizationController.authorize(getRequest());

    assertEquals(HttpStatus.OK, response.getStatusCode());

    DecisionDTO body = (DecisionDTO) response.getBody();
    assertFalse(body.isApproved());
    assertNull(body.getPowerOfAttorney());
    assertNull(body.getFaculty());
    assertNull(body.getSigningRule());
    assertEquals(1, body.getObservations().size());
    assertEquals("NO_FACULTY", body.getObservations().get(0).getCode());
    assertEquals("No power of attorney in force grants PAY on TAX_OBLIGATION",
        body.getObservations().get(0).getMessage());
  }

  @Test
  void isBadRequest() {
    Mockito.when(authorizationService.authorize(ArgumentMatchers.any(AuthorizationRequestDTO.class)))
        .thenThrow(new IllegalArgumentException());
    ResponseEntity<?> response = authorizationController.authorize(getRequest());
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  private AuthorizationRequestDTO getRequest() {
    AuthorizationRequestDTO request = new AuthorizationRequestDTO();
    request.setCompanyId(1L);
    request.setSignersId(List.of(1L, 2L));
    request.setAction(FacultyAction.PAY);
    request.setObject(FacultyObject.TAX_OBLIGATION);
    return request;
  }

}
