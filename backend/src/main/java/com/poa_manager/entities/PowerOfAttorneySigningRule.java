package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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

    @Column(nullable = false)
    private boolean isException;

    @Column(nullable = false)
    private int priority;

    @OneToMany(mappedBy = "powerOfAttorneySigningRule")
    private List<PowerOfAttorneySigningRuleRequirement> powerOfAttorneySigningRuleRequirements;
}