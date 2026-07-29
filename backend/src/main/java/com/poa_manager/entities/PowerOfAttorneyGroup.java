package com.poa_manager.entities;

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
}
