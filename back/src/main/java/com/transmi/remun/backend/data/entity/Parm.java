package com.transmi.remun.backend.data.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.transmi.remun.service.util.ParmType;

@Entity
public class Parm extends AbstractEntity implements Comparable<Parm>
{

  @NotBlank(message = "{remun.name.required}")
  @NotNull(message = "{remun.name.required}")
  @Size(max = 40)
  private String name;

  @NotNull(message = "{remun.from.date.required}")
  private LocalDate dateFrom;

  @NotNull(message = "{remun.to.date.required}")
  private LocalDate dateTo;

  @NotNull(message = "{remun.type.required}")
  @Enumerated(EnumType.STRING)
  private ParmType type;

  @NotNull(message = "{remun.value.required}")
  private String value;

  @Size(max = 255)
  private String comment;

  // --------- Constructors ------------
  public Parm()
  {}

  // ----------- getters & setters --------------
  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public LocalDate getDateFrom() { return dateFrom; }

  public void setDateFrom(LocalDate dateFrom) { this.dateFrom = dateFrom; }

  public LocalDate getDateTo() { return dateTo; }

  public void setDateTo(LocalDate dateTo) { this.dateTo = dateTo; }

  public ParmType getType() { return type; }

  public void setType(ParmType type) { this.type = type; }

  public String getValue() { return value; }

  public void setValue(String value) { this.value = value; }

  public String getComment() { return comment; }

  public void setComment(String comment) { this.comment = comment; }

  public boolean withinPeriod(LocalDate date) { return date.equals(dateFrom) || date.equals(dateTo) ||
      (date.isAfter(dateFrom) && date.isBefore(dateTo)); }

  public Parm createParm() { return new Parm(); }

  // --------------- Object methods ---------------------
  @Override
  public String toString() { return "Parm{" + super.toString() + "] name[" + name + "] fromDate[" + dateFrom + "] toDate[" + dateTo + "]" +
      " type[" + type + "] value[" + value + "] comment[" + comment + "]}"; }

  @Override
  public int compareTo(Parm that) { return that == null ?
      1 :
      this.getCode().compareTo(that.getCode()); }// compareTo

}// Parm
