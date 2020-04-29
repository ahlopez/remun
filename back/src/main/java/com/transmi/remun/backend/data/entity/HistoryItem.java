package com.transmi.remun.backend.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.transmi.remun.service.util.ContractStatus;

@Entity
public class HistoryItem extends AbstractEntity implements Comparable<HistoryItem>
{

  @NotNull(message = "{remun.status.required}")
  @Enumerated(EnumType.STRING)
  private ContractStatus newState;

  @NotBlank(message = "{remun.comment.required}")
  @NotNull(message = "{remun.comment.required}")
  @Size(max = 255)
  private String message;

  @NotNull(message = "{remun.timestamp.required}")
  private LocalDateTime timestamp;

  @ManyToOne
  @NotNull(message = "{remun.user.required}")
  private User createdBy;

  // --------- Constructors ------------
  public HistoryItem()
  {
    // Empty constructor is needed by Spring Data / JPA
  }

  public HistoryItem(User createdBy, String message)
  {
    this.createdBy = createdBy;
    this.message   = message;
    timestamp      = LocalDateTime.now();

  }

  // ----------- getters & setters --------------
  public ContractStatus getNewState() { return newState; }

  public void setNewState(ContractStatus newState) { this.newState = newState; }

  public String getMessage() { return message; }

  public void setMessage(String message) { this.message = message; }

  public LocalDateTime getTimestamp() { return timestamp; }

  public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

  public User getCreatedBy() { return createdBy; }

  public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

  // --------------- Object methods ---------------------
  @Override
  public String toString() { return "HistoryItem{" + super.toString() + " status[" + newState + "] message[" + message +
      "] timestamp[" + timestamp + "] createdBy[" + createdBy.getCode() + "]}"; }

  @Override
  public int compareTo(HistoryItem that) { return that == null ?
      1 :
      this.timestamp.compareTo(that.timestamp); } // compareTo

}// HistoryItem
