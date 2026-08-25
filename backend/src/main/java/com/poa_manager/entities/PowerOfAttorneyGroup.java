package com.poa_manager.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "powerOfAttorneyGroup")
@Getter
@Setter
@NoArgsConstructor
public class PowerOfAttorneyGroup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "powerAttorneyId", referencedColumnName = "id")
  private PowerOfAttorney powerOfAttorney;

  @Column(nullable = false)
  private String label;

  @OneToMany(mappedBy = "powerOfAttorneyGroup")
  private List<PowerOfAttorneyGroupMember> members;

  public boolean hasMember(Person person) {
    return members.stream().anyMatch(member -> member.getPerson().getId().equals(person.getId()));
  }
}
