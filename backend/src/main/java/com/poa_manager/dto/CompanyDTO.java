package com.poa_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompanyDTO {
  private Long id;
  private String name;
  private String type;
}
