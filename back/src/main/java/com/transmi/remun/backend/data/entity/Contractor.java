package com.transmi.remun.backend.data.entity;

import java.time.LocalDate;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "CONTRATISTA", indexes = { @Index(columnList = "code") })
public class Contractor extends AbstractEntity implements Comparable<Contractor>
{

  @Transient
  private static final Random random = new Random(LocalDate.now().toEpochDay());

  @NotBlank
  @Size(max = 255)
  private String fullName;

  @NotBlank
  @Size(max = 20, message = "{remun.phone.number.invalid}")
  // A simple phone number checker, allowing an optional international prefix
  // plus a variable number of digits that could be separated by dashes or spaces
  @Pattern(regexp = "^(\\+\\d+)?[ -]?(\\d\\d\\d[ -]?)?(\\d+){4,14}$", message = "{remun.phone.number.invalid}")
  private String phoneNumber;

  @Size(max = 255)
  private String details;

  // --------- Constructors ------------
  public Contractor()
  {
    setCode(new Integer(random.nextInt(9999999)).toString());
    setFullName("--Desconocido--");
    setPhoneNumber("333-999-9999");
    setDetails("");
  }// Contractor

  // ----------- getters & setters
  public String getFullName() { return fullName; }

  public void setFullName(String fullName) { this.fullName = fullName; }

  public String getPhoneNumber() { return phoneNumber; }

  public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

  public String getDetails() { return details; }

  public void setDetails(String details) { this.details = details; }

  // --------------- Object methods ---------------------
  @Override
  public String toString() { return "Contractor{" + super.toString() + " name[" + fullName + "] phone[" + phoneNumber + "] details[" + details + "]}"; }

  @Override
  public int compareTo(Contractor that) { return that == null ?
      1 :
      this.fullName.compareTo(that.fullName); }// compareTo

}// Contractor
