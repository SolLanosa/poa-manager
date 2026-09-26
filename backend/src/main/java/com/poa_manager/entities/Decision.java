package com.poa_manager.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Decision {

  public record Observation(String code, String message) {
  }

  private boolean approved;
  private Optional<String> powerOfAttorney = Optional.empty();
  private Optional<String> faculty = Optional.empty();
  private Optional<String> signingRule = Optional.empty();
  private List<Observation> observations = new ArrayList<>();

  public void approve(String powerOfAttorney, String faculty, String signingRule) {
    this.approved = true;
    this.powerOfAttorney = Optional.of(powerOfAttorney);
    this.faculty = Optional.of(faculty);
    this.signingRule = Optional.of(signingRule);
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

  public Optional<String> getPowerOfAttorney() {
    return powerOfAttorney;
  }

  public void setPowerOfAttorney(Optional<String> powerOfAttorney) {
    this.powerOfAttorney = powerOfAttorney;
  }

  public Optional<String> getFaculty() {
    return faculty;
  }

  public void setFaculty(Optional<String> faculty) {
    this.faculty = faculty;
  }

  public Optional<String> getSigningRule() {
    return signingRule;
  }

  public void setSigningRule(Optional<String> signingRule) {
    this.signingRule = signingRule;
  }

  public List<Observation> getObservations() {
    return observations;
  }

  public void setObservations(List<Observation> observations) {
    this.observations = observations;
  }

}
