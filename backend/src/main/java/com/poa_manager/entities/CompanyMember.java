package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companyMember")
@Getter
@Setter
@NoArgsConstructor
public class CompanyMember {
  @EmbeddedId
  private CompanyMemberId id = new CompanyMemberId();

  @ManyToOne
  @MapsId("companyId")
  @JoinColumn(name = "companyId", referencedColumnName = "id")
  private Company company;

  @ManyToOne
  @MapsId("personId")
  @JoinColumn(name = "personId", referencedColumnName = "id")
  private Person person;
}