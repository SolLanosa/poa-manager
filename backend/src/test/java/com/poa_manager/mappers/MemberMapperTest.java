package com.poa_manager.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import com.poa_manager.dto.MemberDTO;
import com.poa_manager.entities.Person;

public class MemberMapperTest {
  @Test
  void correctlyMapsMembers() {
    Person person = new Person();
    person.setId(1L);
    person.setFirstName("Alice");
    person.setLastName("Doe");
    person.setNationalId("1245");
    MemberDTO dto = MemberMapper.toDTO(person);
    assertEquals(person.getId(), dto.getId());
    assertEquals(person.getLastName(), dto.getLastName());
    assertEquals(person.getFirstName(), dto.getFirstName());
    assertEquals(person.getNationalId(), dto.getNationalId());
  }
}
