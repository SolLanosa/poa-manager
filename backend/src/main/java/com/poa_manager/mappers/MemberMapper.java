package com.poa_manager.mappers;

import com.poa_manager.dto.MemberDTO;
import com.poa_manager.entities.Person;

public class MemberMapper {
    public static MemberDTO toDTO(Person person) {
        return new MemberDTO(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getNationalId()
        );
    }
}
