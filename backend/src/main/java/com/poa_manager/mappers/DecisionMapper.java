package com.poa_manager.mappers;

import com.poa_manager.dto.DecisionDTO;
import com.poa_manager.entities.Decision;

public class DecisionMapper {
  public static DecisionDTO toDTO(Decision decision) {
    return new DecisionDTO(decision.isApproved(),
        decision.getPowerOfAttorney().orElse(null),
        decision.getFaculty().orElse(null),
        decision.getSigningRule().orElse(null),
        decision.getObservations().stream().map(DecisionMapper::toDTO).toList());
  }

  private static DecisionDTO.ObservationDTO toDTO(Decision.Observation observation) {
    return new DecisionDTO.ObservationDTO(observation.code(), observation.message());
  }
}
