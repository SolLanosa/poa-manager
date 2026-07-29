package com.poa_manager.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "powerOfAttorneyGroupMember")
@Getter
@Setter
@NoArgsConstructor
public class PowerOfAttorneyGroupMember {
    @EmbeddedId
    private PowerOfAttorneyGroupMemberId id = new PowerOfAttorneyGroupMemberId();

    @ManyToOne
    @MapsId("powerOfAttorneyGroupId")
    @JoinColumn(name = "powerOfAttorneyGroupId", referencedColumnName = "id")
    private PowerOfAttorneyGroup powerOfAttorneyGroup;

    @ManyToOne
    @MapsId("personId")
    @JoinColumn(name = "personId", referencedColumnName = "id")
    private Person person;
}