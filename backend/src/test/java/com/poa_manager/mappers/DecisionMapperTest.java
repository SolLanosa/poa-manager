package com.poa_manager.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import com.poa_manager.dto.DecisionDTO;
import com.poa_manager.entities.Decision;

public class DecisionMapperTest {
  @Test
  void correctlyMapsDecision() {
    Decision decision = new Decision();
    decision.approve("Banking", "Faculty", "Rule");
    decision.addObservation("APPROVED", "");

    DecisionDTO dto = DecisionMapper.toDTO(decision);

    assertEquals("Banking", dto.getPowerOfAttorney());
    assertEquals("Faculty", dto.getFaculty());
    assertEquals("Rule", dto.getSigningRule());
    assertEquals("APPROVED", dto.getObservations().get(0).getCode());
    assertEquals("", dto.getObservations().get((0)).getMessage());
  }
}
