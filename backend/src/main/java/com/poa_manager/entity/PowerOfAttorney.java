package com.poa_manager.entity;

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

    @OneToMany(mappedBy = "powerOfAttorney")
    private List<PowerOfAttorneyGroup> powerOfAttorneyGroups;
}
