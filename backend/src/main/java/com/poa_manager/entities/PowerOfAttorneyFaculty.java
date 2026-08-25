package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "powerOfAttorneyFaculty")
@Getter
@Setter
@NoArgsConstructor
public class PowerOfAttorneyFaculty {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "powerOfAttorneyId", referencedColumnName = "id")
  private PowerOfAttorney powerOfAttorney;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FacultyAction action;

  @Column(columnDefinition = "text")
  private String description;

  @OneToMany(mappedBy = "powerOfAttorneyFaculty")
  private List<PowerOfAttorneySigningRule> signingRules;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FacultyObject objectCategory;

  @OneToMany(mappedBy = "faculty")
  private List<FacultyScopeItem> scopeItems;

  public boolean covers(FacultyAction action, FacultyObject objectCategory) {
    return this.action == action && this.objectCategory == objectCategory;
  }

  public boolean coversItem(String item) {
    if (scopeItems.isEmpty())
      return true;
    return scopeItems.stream().anyMatch(scopeItem -> scopeItem.getExternalRef().equals(item));
  }

  public String printSigningRules(BigDecimal amount, String currency) {
    return signingRules.stream()
        .filter(signingRule -> signingRule.amountFulfills(amount, currency))
        .map(PowerOfAttorneySigningRule::toString)
        .collect(Collectors.joining("\n"));
  }
}