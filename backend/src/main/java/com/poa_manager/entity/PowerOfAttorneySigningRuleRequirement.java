package com.poa_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "powerOfAttorneySigningRuleRequirement")
@Getter
@Setter
@NoArgsConstructor
public class PowerOfAttorneySigningRuleRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "signingRuleId", referencedColumnName = "id")
    private PowerOfAttorneySigningRule powerOfAttorneySigningRule;

    @ManyToOne
    @JoinColumn(name = "powerOfAttorneyGroupId", referencedColumnName = "id")
    private PowerOfAttorneyGroup powerOfAttorneyGroup;

    @Column(nullable = false)
    private int countRequired;
}