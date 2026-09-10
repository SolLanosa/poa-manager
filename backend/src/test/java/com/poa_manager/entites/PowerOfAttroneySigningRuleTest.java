package com.poa_manager.entites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.poa_manager.entities.Person;
import com.poa_manager.entities.PowerOfAttorneyGroup;
import com.poa_manager.entities.PowerOfAttorneyGroupMember;
import com.poa_manager.entities.PowerOfAttorneySigningRule;
import com.poa_manager.entities.PowerOfAttorneySigningRuleRequirement;

public class PowerOfAttroneySigningRuleTest {

  @Test
  void isTrueForAnyValueIfAmountIsNull() {
    PowerOfAttorneySigningRule rule = getSigningRule(null, null, "USD");
    assertTrue(rule.amountFulfills(new BigDecimal("1.00"), "USD"));
    assertTrue(rule.amountFulfills(new BigDecimal("999999999.00"), "USD"));
    assertTrue(rule.amountFulfills(null, "USD"));
  }

  @Test
  void isFalseWhenAmountNullIfHasMaxAmount() {
    PowerOfAttorneySigningRule rule = getSigningRule(null, new BigDecimal("300000.00"), "USD");
    assertFalse(rule.amountFulfills(null, "USD"));
  }

  @Test
  void validateCorrectlyCurrency() {
    PowerOfAttorneySigningRule rule = getSigningRule(null, new BigDecimal("300000.00"), "USD");
    assertFalse(rule.amountFulfills(new BigDecimal("200000.00"), "EUR"));
    assertTrue(rule.amountFulfills(new BigDecimal("200000.00"), "USD"));
  }

  @Test
  void correctlyValidatesAmountsWhenBothMinAndMaxAreSet() {
    PowerOfAttorneySigningRule rule = getSigningRule(new BigDecimal("1000.00"), new BigDecimal("5000.00"), "USD");
    assertTrue(rule.amountFulfills(new BigDecimal("1000.00"), "USD"));
    assertTrue(rule.amountFulfills(new BigDecimal("5000.00"), "USD"));
    assertFalse(rule.amountFulfills(new BigDecimal("999.99"), "USD"));
    assertFalse(rule.amountFulfills(new BigDecimal("5000.01"), "USD"));
  }

  @Test
  void isTrueWhenOnlyOneSignerFromTheRequiredGroup() {
    Person alice = getPerson(1L);
    PowerOfAttorneyGroup groupA = getGroup("Group A", alice);
    PowerOfAttorneySigningRule rule = getSigningRule(null, null, "USD", getSigingRuleRequirement(groupA, 1));
    assertTrue(rule.isSignedBy(List.of(alice)));
  }

  @Test
  void isFalseWhenNotEnoughSigners() {
    Person alice = getPerson(1L);
    Person bob = getPerson(2L);
    PowerOfAttorneyGroup groupA = getGroup("Group A", alice, bob);
    PowerOfAttorneySigningRule rule = getSigningRule(null, null, "USD", getSigingRuleRequirement(groupA, 2));
    assertFalse(rule.isSignedBy(List.of(alice)));
  }

  @Test
  void isTrueWhenAllSignersFulfilledWithSignersOutsideGroup() {
    Person alice = getPerson(1L);
    Person outsider = getPerson(2L);
    PowerOfAttorneyGroup groupA = getGroup("Group A", alice);
    PowerOfAttorneySigningRule rule = getSigningRule(null, null, "USD", getSigingRuleRequirement(groupA, 1));
    assertTrue(rule.isSignedBy(List.of(outsider, alice)));
  }

  @Test
  void isFalseWhenSignerOutsideGroup() {
    Person alice = getPerson(1L);
    Person outsider = getPerson(2L);
    PowerOfAttorneyGroup groupA = getGroup("Group A", alice);
    PowerOfAttorneySigningRule rule = getSigningRule(null, null, "USD", getSigingRuleRequirement(groupA, 1));
    assertFalse(rule.isSignedBy(List.of(outsider)));
  }

  @Test
  void isFalseWhenNoRequirements() {
    Person alice = getPerson(1L);
    PowerOfAttorneySigningRule rule = getSigningRule(null, null, "USD");
    assertEquals(rule.getPowerOfAttorneySigningRuleRequirements().size(), 0);
    assertFalse(rule.isSignedBy(List.of(alice)));
  }

  @Test
  void isFalseWhenNoSigners() {
    Person alice = getPerson(1L);
    PowerOfAttorneyGroup groupA = getGroup("Group A", alice);
    PowerOfAttorneySigningRule rule = getSigningRule(null, null, "USD", getSigingRuleRequirement(groupA, 1));
    List<Person> signers = List.of();
    assertEquals(signers.size(), 0);
    assertFalse(rule.isSignedBy(signers));
  }

  @Test
  void itDoesntCountOnePersonInTwoGroups() {
    Person alice = getPerson(1L);
    Person bob = getPerson(2L);
    PowerOfAttorneyGroup groupA = getGroup("Group A", alice);
    PowerOfAttorneyGroup groupB = getGroup("Group B", alice, bob);
    PowerOfAttorneySigningRule rule = getSigningRule(null, null, "USD",
        getSigingRuleRequirement(groupA, 1), getSigingRuleRequirement(groupB, 1));
    assertFalse(rule.isSignedBy(List.of(alice)));
    assertTrue(rule.isSignedBy(List.of(alice, bob)));
  }

  private Person getPerson(Long id) {
    Person person = new Person();
    person.setId(id);
    return person;
  }

  private PowerOfAttorneyGroupMember getMember(PowerOfAttorneyGroup group, Person person) {
    PowerOfAttorneyGroupMember member = new PowerOfAttorneyGroupMember();
    member.setPowerOfAttorneyGroup(group);
    member.setPerson(person);
    return member;
  }

  private PowerOfAttorneyGroup getGroup(String label, Person... people) {
    PowerOfAttorneyGroup group = new PowerOfAttorneyGroup();
    group.setLabel(label);
    group.setMembers(new ArrayList<>());
    for (Person person : people) {
      group.getMembers().add(getMember(group, person));
    }
    return group;
  }

  private PowerOfAttorneySigningRuleRequirement getSigingRuleRequirement(PowerOfAttorneyGroup group,
      int countRequired) {
    PowerOfAttorneySigningRuleRequirement requirement = new PowerOfAttorneySigningRuleRequirement();
    requirement.setPowerOfAttorneyGroup(group);
    requirement.setCountRequired(countRequired);
    return requirement;
  }

  private PowerOfAttorneySigningRule getSigningRule(BigDecimal minAmount, BigDecimal maxAmount, String currency,
      PowerOfAttorneySigningRuleRequirement... requirements) {
    PowerOfAttorneySigningRule rule = new PowerOfAttorneySigningRule();
    rule.setMinAmount(minAmount);
    rule.setMaxAmount(maxAmount);
    rule.setCurrency(currency);
    rule.setPowerOfAttorneySigningRuleRequirements(new ArrayList<>(Arrays.asList(requirements)));
    return rule;
  }
}
