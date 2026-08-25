package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor

public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "company")
  private List<PowerOfAttorney> powerOfAttorneys;

  @OneToMany(mappedBy = "company")
  private List<CompanyMember> companyMembers;
}
