package com.poa_manager.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poa_manager.dto.AuthorizationRequestDTO;
import com.poa_manager.entities.Decision;
import com.poa_manager.mappers.DecisionMapper;
import com.poa_manager.services.AuthorizationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authorizations")
public class AuthorizationController {

  private final AuthorizationService authorizationService;

  @PostMapping("/")
  public ResponseEntity<?> authorize(@RequestBody AuthorizationRequestDTO body) {
    if (body.getCompanyId() == null || body.getAction() == null || body.getObject() == null) {
      return ResponseEntity.badRequest().body("companyId, action and object are required");
    }

    if (body.getSignersId() == null || body.getSignersId().isEmpty()) {
      return ResponseEntity.badRequest().body("One signer is required");
    }

    try {
      Decision decision = authorizationService.authorize(body);
      return ResponseEntity.ok(DecisionMapper.toDTO(decision));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
