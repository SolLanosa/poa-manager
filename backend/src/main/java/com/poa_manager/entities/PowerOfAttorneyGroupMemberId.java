// https://www.baeldung.com/jpa-many-to-many
// https://www.baeldung.com/java-lombok-equalsandhashcode

package com.poa_manager.entities;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PowerOfAttorneyGroupMemberId implements Serializable {
    private Long powerOfAttorneyGroupId;
    private Long personId;
}