package com.poa_manager.entites;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.poa_manager.entities.Company;
import com.poa_manager.entities.PowerOfAttorney;

public class PowerOfAttorneyTest {
  @Mock
  private Company company;

  @Test
  void isNotActiveWhenRevoked() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2024, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    powerOfAttorney.setRevokedEffective(LocalDate.of(2026, 6, 17).atStartOfDay());
    assertFalse(powerOfAttorney.isActive((LocalDate.of(2026, 8, 30).atStartOfDay())));
  }

  @Test
  void isActiveWhenNoDatesSet() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    assertTrue(powerOfAttorney.isActive((LocalDate.of(2026, 8, 30).atStartOfDay())));
  }

  @Test
  void isNotActiveWhenDateNull() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2024, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    powerOfAttorney.setRevokedEffective(LocalDate.of(2026, 6, 17).atStartOfDay());
    assertFalse(powerOfAttorney.isActive(null));
  }

  @Test
  void isNotActiveWhenRevokedSameDay() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2024, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    powerOfAttorney.setRevokedEffective(LocalDate.of(2026, 6, 17).atStartOfDay());

    assertFalse(powerOfAttorney.isActive((LocalDate.of(2026, 6, 17).atStartOfDay())));
  }

  @Test
  void isNotActiveWhenStillNotValidFrom() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2026, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    powerOfAttorney.setRevokedEffective(LocalDate.of(2026, 6, 17).atStartOfDay());
    assertFalse(powerOfAttorney.isActive((LocalDate.of(2026, 6, 17).atStartOfDay())));
  }

  @Test
  void isNotActiveNextDayOfValidity() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2026, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    assertFalse(powerOfAttorney.isActive((LocalDate.of(2028, 1, 1).atStartOfDay())));
  }

  @Test
  void isValidUntilPreviousDay() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2026, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    assertTrue(powerOfAttorney.isActive((LocalDate.of(2027, 12, 30).atStartOfDay())));
  }

  @Test
  void isActiveWhenValidUntilSameDay() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2026, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    assertTrue(powerOfAttorney.isActive((LocalDate.of(2027, 12, 31).atStartOfDay())));
  }

  @Test
  void isNotValidFromPreviousDay() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2026, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    assertFalse(powerOfAttorney.isActive((LocalDate.of(2026, 12, 8).atStartOfDay())));
  }

  @Test
  void isValidFromSameDay() {
    PowerOfAttorney powerOfAttorney = new PowerOfAttorney();
    powerOfAttorney.setCompany(company);
    powerOfAttorney.setGrantedOn(LocalDate.of(2024, 11, 14).atStartOfDay());
    powerOfAttorney.setValidFrom(LocalDate.of(2026, 12, 9).atStartOfDay());
    powerOfAttorney.setValidUntil(LocalDate.of(2027, 12, 31).atStartOfDay());
    assertTrue(powerOfAttorney.isActive((LocalDate.of(2026, 12, 9).atStartOfDay())));
  }

}
