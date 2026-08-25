package com.poa_manager.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Decision {

  public record Observation(String code, String message) {
  }

  private boolean approved;
  private Optional<Long> powerOfAttorneyId = Optional.empty();
  private Optional<Long> facultyId = Optional.empty();
  private Optional<Long> signingRuleId = Optional.empty();
  private List<Observation> observations = new ArrayList<>();

  public void approve(Long powerOfAttorneyId, Long facultyId, Long signingRuleId) {
    this.approved = true;
    this.powerOfAttorneyId = Optional.of(powerOfAttorneyId);
    this.facultyId = Optional.of(facultyId);
    this.signingRuleId = Optional.of(signingRuleId);
  }

  public void addObservation(String code, String message) {
    observations.add(new Observation(code, message));
  }

  public boolean isApproved() {
    return approved;
  }

  public void setApproved(boolean approved) {
    this.approved = approved;
  }

  public Optional<Long> getPowerOfAttorneyId() {
    return powerOfAttorneyId;
  }

  public void setPowerOfAttorneyId(Optional<Long> powerOfAttorneyId) {
    this.powerOfAttorneyId = powerOfAttorneyId;
  }

  public Optional<Long> getFacultyId() {
    return facultyId;
  }

  public void setFacultyId(Optional<Long> facultyId) {
    this.facultyId = facultyId;
  }

  public Optional<Long> getSigningRuleId() {
    return signingRuleId;
  }

  public void setSigningRuleId(Optional<Long> signingRuleId) {
    this.signingRuleId = signingRuleId;
  }

  public List<Observation> getObservations() {
    return observations;
  }

  public void setObservations(List<Observation> observations) {
    this.observations = observations;
  }

}
