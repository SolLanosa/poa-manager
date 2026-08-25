package com.poa_manager.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorizationRequest {
  Company company;
  ArrayList<Person> signers;
  FacultyAction action;
  FacultyObject object;
  String itemRef;
  BigDecimal amount;
  String currency;
  LocalDateTime date;

  public ArrayList<Person> getSigners() {
    return signers;
  }

  public void setSigners(ArrayList<Person> signers) {
    this.signers = signers;
  }

  public FacultyAction getAction() {
    return action;
  }

  public void setAction(FacultyAction action) {
    this.action = action;
  }

  public FacultyObject getObject() {
    return object;
  }

  public void setObject(FacultyObject object) {
    this.object = object;
  }

  public String getItemRef() {
    return itemRef;
  }

  public void setItemRef(String itemRef) {
    this.itemRef = itemRef;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }
}
