package com.poa_manager.entites;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.poa_manager.entities.FacultyAction;
import com.poa_manager.entities.FacultyObject;
import com.poa_manager.entities.FacultyScopeItem;
import com.poa_manager.entities.PowerOfAttorneyFaculty;

public class PowerOfAttorneyFacultyTest {

  @Test
  void isTrueWhenCoversSameObjectAndAction() {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setAction(FacultyAction.OPEN);
    faculty.setObjectCategory(FacultyObject.BANK_ACCOUNT);
    assertTrue(faculty.covers(FacultyAction.OPEN, FacultyObject.BANK_ACCOUNT));
  }

  @Test
  void isFalseWhenActionIsDifferent() {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setAction(FacultyAction.OPEN);
    faculty.setObjectCategory(FacultyObject.BANK_ACCOUNT);
    assertFalse(faculty.covers(FacultyAction.ANSWER, FacultyObject.BANK_ACCOUNT));
  }

  @Test
  void isFalseWhenObjectIsDifferent() {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setAction(FacultyAction.OPEN);
    faculty.setObjectCategory(FacultyObject.BANK_ACCOUNT);
    assertFalse(faculty.covers(FacultyAction.OPEN, FacultyObject.CARGO));
  }

  @Test
  void isFalseWhenObjectAndActionAreDifferent() {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setAction(FacultyAction.OPEN);
    faculty.setObjectCategory(FacultyObject.BANK_ACCOUNT);
    assertFalse(faculty.covers(FacultyAction.AMORTIZE, FacultyObject.CARGO));
  }

  @Test
  void isTrueForAnyObjectWhenNoScopeItem() {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setAction(FacultyAction.OPEN);
    faculty.setObjectCategory(FacultyObject.BANK_ACCOUNT);
    faculty.setScopeItems(new ArrayList<>());
    assertTrue(faculty.coversItem("bank 1234"));
    assertTrue(faculty.coversItem("bank 5674"));
  }

  @Test
  void isTrueWhenHasScopeItemAndSame() {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setAction(FacultyAction.OPEN);
    faculty.setObjectCategory(FacultyObject.BANK_ACCOUNT);
    FacultyScopeItem scopeItem = new FacultyScopeItem();
    scopeItem.setExternalRef("bank 1234");
    faculty.setScopeItems(List.of(scopeItem));
    assertTrue(faculty.coversItem("bank 1234"));

  }

  @Test
  void isFalseWhenHasScopeItemAndNotSame() {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setAction(FacultyAction.OPEN);
    faculty.setObjectCategory(FacultyObject.BANK_ACCOUNT);
    FacultyScopeItem scopeItem = new FacultyScopeItem();
    scopeItem.setExternalRef("bank 1234");
    faculty.setScopeItems(List.of(scopeItem));
    assertFalse(faculty.coversItem("bank 5674"));
  }

  @Test
  void isFalseWhenHasScopeItemAndNoRef() {
    PowerOfAttorneyFaculty faculty = new PowerOfAttorneyFaculty();
    faculty.setAction(FacultyAction.OPEN);
    faculty.setObjectCategory(FacultyObject.BANK_ACCOUNT);
    FacultyScopeItem scopeItem = new FacultyScopeItem();
    scopeItem.setExternalRef("bank 1234");
    faculty.setScopeItems(List.of(scopeItem));
    assertFalse(faculty.coversItem(null));
  }

}
