package com.poa_manager.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DecisionDTO {

  @Getter
  @AllArgsConstructor
  public static class ObservationDTO {
    private String code;
    private String message;
  }

  private boolean approved;
  private String powerOfAttorney;
  private String faculty;
  private String signingRule;
  private List<ObservationDTO> observations;

}
