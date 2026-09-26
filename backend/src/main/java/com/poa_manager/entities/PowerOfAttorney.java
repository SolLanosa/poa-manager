package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "powerOfAttorney")
@Getter
@Setter
@NoArgsConstructor
public class PowerOfAttorney {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "companyId", referencedColumnName = "id")
  private Company company;

  @Column
  LocalDateTime grantedOn;

  @Column
  LocalDateTime validFrom;

  @Column
  LocalDateTime validUntil;

  @Column
  LocalDateTime revokedEffective;

  @Column
  String reference;

  @Column
  String description;

  @OneToMany(mappedBy = "powerOfAttorney")
  private List<PowerOfAttorneyGroup> powerOfAttorneyGroups;

  @OneToMany(mappedBy = "powerOfAttorney")
  private List<PowerOfAttorneyFaculty> faculties;

  public boolean isActive(LocalDateTime when) {
    if (when == null)
      return false;
    if (validFrom != null && when.isBefore(validFrom))
      return false;
    if (validUntil != null && when.isAfter(validUntil))
      return false;
    if (revokedEffective != null && !when.isBefore(revokedEffective))
      return false;
    return true;
  }

  public String toString() {
    return description + " (" + reference + ")";
  }
}
