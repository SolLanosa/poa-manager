package com.poa_manager.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import com.poa_manager.dto.DecisionDTO;
import com.poa_manager.entities.Decision;

public class DecisionMapperTest {
  @Test
  void correctlyMapsDecision() {
    Decision decision = new Decision();
    decision.approve(1L, 2L, 3L);
    decision.addObservation("APPROVED", "Signed under power of attorney 1, 3 signing rule");

    DecisionDTO dto = DecisionMapper.toDTO(decision);

    assertEquals(1L, dto.getPowerOfAttorneyId());
    assertEquals(2L, dto.getFacultyId());
    assertEquals(3L, dto.getSigningRuleId());
    assertEquals("APPROVED", dto.getObservations().get(0).getCode());
    assertEquals("Signed under power of attorney 1, 3 signing rule", dto.getObservations().get((0)).getMessage());
  }
}
