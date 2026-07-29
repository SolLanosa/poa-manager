package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@Table(name = "facultyScopeItem")
@Getter
@Setter
@NoArgsConstructor
public class FacultyScopeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "facultyId", referencedColumnName = "id")
    private PowerOfAttorneyFaculty faculty;

    @Column(nullable = false)
    private String externalRef;

    @Column(columnDefinition = "text")
    private String label;
}