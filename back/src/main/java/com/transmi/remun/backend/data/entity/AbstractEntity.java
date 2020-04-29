package com.transmi.remun.backend.data.entity;

import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AbstractEntity
{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Version
  private int version;

  @NotNull(message = "{remun.code.required}")
  private String code;

  // ---------------- Getters y Setters -------------------

  public Long getId() { return id; }

  public boolean isPersisted() { return id != null; }

  public int getVersion() { return version; }

  public String getCode() { return code; }

  public void setCode(String code) { this.code = code; }

  // ---------- Object methods ------------------

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
    AbstractEntity that = (AbstractEntity) o;
    if (this.getId() == null || that.getId() == null)
    {
      return false;
    }
    return this.getId().equals(that.getId());

  }// equals

  @Override
  public int hashCode() { return Objects.hash(id, version); }

  @Override
  public String toString() { return "id[" + id + "] code[" + code + "] version[" + version + "]"; }

}// AbstractEntity
