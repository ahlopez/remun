package com.transmi.remun.backend.data.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "UserInfo")
public class User extends AbstractEntity implements Comparable<User>
{

  @NotEmpty(message = "{remun.email.required}")
  @Email
  @Size(max = 255)
  @Column(unique = true)
  private String email;

  @NotNull
  @Size(min = 4, max = 255)
  private String passwordHash;

  @NotBlank(message = "{remun.name.required}")
  @NotEmpty(message = "{remun.name.required}")
  @Size(max = 255)
  private String firstName;

  @NotBlank(message = "{remun.name.required}")
  @NotEmpty(message = "{remun.name.required}")
  @Size(max = 255)
  private String lastName;

  @NotBlank
  @NotBlank(message = "{remun.role.required}")
  @NotEmpty(message = "{remun.role.required}")
  @Size(max = 255)
  private String role;

  private boolean locked = false;

  @PrePersist
  @PreUpdate
  public void prepareData() {
    this.email = email == null ?
        null :
        email.toLowerCase();

  }

  // ----------------- Constructor -----------------
  public User()
  {

  }

  // --------------- Getters & Setters -----------------
  public String getPasswordHash() { return passwordHash; }

  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

  public String getFirstName() { return firstName; }

  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }

  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getRole() { return role; }

  public void setRole(String role) { this.role = role; }

  public String getEmail() { return email; }

  public void setEmail(String email) {
    this.email = email;
    this.setCode(email);
  }// setEmail

  public boolean isLocked() { return locked; }

  public void setLocked(boolean locked) { this.locked = locked; }

  // --------------- Object ------------------

  @Override
  public boolean equals(Object o) {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    if (!super.equals(o))
    {
      return false;
    }
    User that = (User) o;
    return locked == that.locked &&
        Objects.equals(email, that.email) &&
        Objects.equals(firstName, that.firstName) &&
        Objects.equals(lastName, that.lastName) &&
        Objects.equals(role, that.role);

  }// equals

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), email, firstName, lastName, role, locked);

  }

  @Override
  public String toString() { return "User{" + super.toString() + " firstName[" + firstName + "] lastName[" + lastName + "] role[" + role + "] email[" + email + "]}"; }// toString

  @Override
  public int compareTo(User that) { return that == null ?
      1 :
      (this.lastName + ":" + this.firstName).compareTo(that.lastName + ":" + that.firstName); }// compareTo

}// User
