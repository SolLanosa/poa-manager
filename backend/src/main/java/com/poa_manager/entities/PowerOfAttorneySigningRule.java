package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "powerOfAttorneySigningRule")
@Getter
@Setter
@NoArgsConstructor
public class PowerOfAttorneySigningRule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "facultyId", referencedColumnName = "id")
  private PowerOfAttorneyFaculty powerOfAttorneyFaculty;

  @Column(precision = 19, scale = 2)
  private BigDecimal minAmount;

  @Column(precision = 19, scale = 2)
  private BigDecimal maxAmount;

  @Column(length = 3)
  private String currency;

  @OneToMany(mappedBy = "powerOfAttorneySigningRule")
  private List<PowerOfAttorneySigningRuleRequirement> powerOfAttorneySigningRuleRequirements;

  // checks if the provided list of signers fulfills the signing rules
  // requirements.
  public boolean isSignedBy(List<Person> signers) {
    if (powerOfAttorneySigningRuleRequirements.isEmpty())
      return false;

    List<Person> pendingSigners = new ArrayList<>(signers);
    // loop each requirement and check its group count is fullfilled
    for (PowerOfAttorneySigningRuleRequirement requirement : powerOfAttorneySigningRuleRequirements) {
      PowerOfAttorneyGroup expectdGroup = requirement.getPowerOfAttorneyGroup();
      int expectedCount = requirement.getCountRequired();
      List<Person> groupSigners = pendingSigners.stream()
          .filter(expectdGroup::hasMember)
          .limit(expectedCount)
          .toList();
      if (groupSigners.size() < expectedCount)
        return false;
      // remove checked signers so we don't double count
      pendingSigners.removeAll(groupSigners);
    }
    return true;
  }

  public String toString() {
    return powerOfAttorneySigningRuleRequirements.stream()
        .map(requirement -> requirement.getCountRequired() + " of "
            + requirement.getPowerOfAttorneyGroup().getLabel())
        .collect(Collectors.joining(" + "));
  }

  public boolean amountFulfills(BigDecimal amount, String currency) {
    if (getCurrency() != null && !getCurrency().equalsIgnoreCase(currency == null ? "" : currency)) {
      return false;
    }
    boolean hasLimit = getMinAmount() != null || getMaxAmount() != null;
    if (!hasLimit)
      return true;
    if (amount == null)
      return false;
    if (getMinAmount() != null && amount.compareTo(getMinAmount()) < 0)
      return false;
    if (getMaxAmount() != null && amount.compareTo(getMaxAmount()) > 0)
      return false;
    return true;
  }
}