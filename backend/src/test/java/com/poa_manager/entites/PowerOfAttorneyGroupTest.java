package com.poa_manager.entites;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.poa_manager.entities.Person;
import com.poa_manager.entities.PowerOfAttorneyGroup;
import com.poa_manager.entities.PowerOfAttorneyGroupMember;

public class PowerOfAttorneyGroupTest {
  @Test
  void isTrueWhenIsMember() {
    PowerOfAttorneyGroup powerOfAttorneyGroup = new PowerOfAttorneyGroup();
    PowerOfAttorneyGroupMember powerOfAttorneyGroupMember = new PowerOfAttorneyGroupMember();

    Person person = new Person();
    person.setId(1L);
    powerOfAttorneyGroupMember.setPerson(person);
    powerOfAttorneyGroup.setMembers(List.of(powerOfAttorneyGroupMember));
    assertTrue(powerOfAttorneyGroup.hasMember(person));
  }

  @Test
  void isFalseWhenIsNotMember() {
    PowerOfAttorneyGroup powerOfAttorneyGroup = new PowerOfAttorneyGroup();
    PowerOfAttorneyGroupMember powerOfAttorneyGroupMember = new PowerOfAttorneyGroupMember();

    Person person = new Person();
    person.setId(1L);
    Person notMember = new Person();
    notMember.setId(2L);
    powerOfAttorneyGroupMember.setPerson(person);
    powerOfAttorneyGroup.setMembers(List.of(powerOfAttorneyGroupMember));
    assertFalse(powerOfAttorneyGroup.hasMember(notMember));
  }
}
