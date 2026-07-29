package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "powerOfAttorneyFaculty")
@Getter
@Setter
@NoArgsConstructor
public class PowerOfAttorneyFaculty  {
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
}