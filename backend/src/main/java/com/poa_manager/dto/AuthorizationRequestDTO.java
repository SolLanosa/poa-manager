package com.poa_manager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.poa_manager.entities.FacultyAction;
import com.poa_manager.entities.FacultyObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorizationRequestDTO {
  private Long companyId;
  private List<Long> signersId;
  private FacultyAction action;
  private FacultyObject object;
  private String itemRef;
  private BigDecimal amount;
  private String currency;
  private LocalDateTime date;
}
