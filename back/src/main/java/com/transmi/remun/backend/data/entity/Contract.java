package com.transmi.remun.backend.data.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.BatchSize;

import com.transmi.remun.backend.service.ContractorService;
import com.transmi.remun.service.util.ContractStatus;
import com.transmi.remun.service.util.TransmiPhase;

@NamedEntityGraphs({
    @NamedEntityGraph(
        name = Contract.ENTITY_GRAPH_BRIEF,
        attributeNodes = {
            @NamedAttributeNode("parms")
        }),
    @NamedEntityGraph(
        name = Contract.ENTITY_GRAPH_FULL,
        attributeNodes = {
            @NamedAttributeNode("parms"),
            @NamedAttributeNode("history")
        }) })
@Entity
@Table(name = "CONTRATO", indexes = { @Index(columnList = "fase"), @Index(columnList = "code") })
public class Contract extends AbstractEntity implements Comparable<Contract>
{

  public static final String ENTITY_GRAPH_BRIEF = "Contract.brief";
  public static final String ENTITY_GRAPH_FULL = "Contract.full";

  @NotNull(message = "{remun.phase.required}")
  @Enumerated(EnumType.STRING)
  private TransmiPhase fase;

  @NotNull(message = "{remun.name.required}")
  private String name;

  // @NotNull(message = "{remun.contractor.required}")
  @ManyToOne(cascade = CascadeType.ALL)
  private Contractor contractor;

  @NotNull(message = "{remun.from.date.required}")
  private LocalDate fromDate;

  @NotNull(message = "{remun.to.date.required}")
  private LocalDate toDate;

  @Transient
  private boolean vigente;

  @NotNull(message = "{remun.status.required}")
  @Enumerated(EnumType.STRING)
  public ContractStatus status;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @OrderColumn
  @JoinColumn
  @BatchSize(size = 100)
  @Valid
  // @NotEmpty
  private List<Parm> parms;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @OrderColumn
  @JoinColumn
  @BatchSize(size = 100)
  private List<HistoryItem> history;

  // ------------- Constructors ------------------

  public Contract()
  {
    this.contractor = ContractorService.getGlobalContractor();
    this.status     = ContractStatus.CERRADO;
    this.parms      = new ArrayList<>();
  }// ContractEntity

  public Contract(Contractor contractor, User user)
  {
    this.status = ContractStatus.VIGENTE;
    setContractor(contractor);
    addHistoryItem(user, "Nuevo contrato");
    this.parms = new ArrayList<>();
  }// ContractEntity

  // -------------- Getters & Setters ----------------

  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public TransmiPhase getFase() { return fase; }

  public void setFase(TransmiPhase fase) { this.fase = fase; }

  public LocalDate getFromDate() { return fromDate; }

  public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }

  public LocalDate getToDate() { return toDate; }

  public void setToDate(LocalDate toDate) { this.toDate = toDate; }

  public boolean isVigente() {
    LocalDate date = LocalDate.now();
    vigente = status.equals(ContractStatus.VIGENTE) &&
        (date.equals(fromDate) || date.equals(toDate) || (date.isAfter(fromDate) && date.isBefore(toDate)));
    return vigente;
  }// isVigente

  public boolean isVigente(LocalDate date) {
    setVigente(date);
    return vigente;
  }

  public void setVigente(LocalDate date) { this.vigente = (status.equals(ContractStatus.VIGENTE) && date != null &&
      (date.equals(fromDate) || date.equals(toDate) || (date.isAfter(fromDate) && date.isBefore(toDate)))); }

  public Contractor getContractor() { return contractor; }

  public void setContractor(Contractor contractor) { this.contractor = contractor; }

  public List<Parm> getParms() { return parms; }

  public Integer getNumberOfParms() { return parms.size(); }

  public void setParms(List<Parm> parms) { this.parms = parms; }

  public List<HistoryItem> getHistory() { return history; }

  public void setHistory(List<HistoryItem> history) { this.history = history; }

  public void addHistoryItem(User createdBy, String comment) {
    HistoryItem item = new HistoryItem(createdBy, comment);
    item.setNewState(status);
    if (history == null)
    {
      history = new LinkedList<>();
    }
    history.add(item);
  }// addHistoryItem

  public ContractStatus getStatus() { return status; }

  public void setStatus(ContractStatus status) { this.status = status; }

  public void changeState(User user, ContractStatus status) {
    boolean createHistory = this.status != status && this.status != null && status != null;
    this.status = status;
    if (createHistory)
    {
      addHistoryItem(user, "Contract " + status);
    }
  }// changeState

  // --------------- Object methods ---------------------
  @Override
  public String toString() {
    StringBuffer prms = new StringBuffer();
    parms.forEach(p-> prms.append(p.getCode()).append(" "));
    return "Contract{" + super.toString() + " fase[" + fase.toString() + "] fromDate[" + fromDate + "] toDate[" + toDate + "]" +
        " contractor[" + contractor.getFullName() + "]" +
        " parms{" + prms.toString() + "} status[" + status + "]" +
        " history size{" + history.size() + "} items}";
  }// toString

  @Override
  public int compareTo(Contract that) {
    String key1 = this.fase.toString() + ":" + this.getCode();
    String key2 = that.fase.toString() + ":" + that.getCode();
    return key1.compareTo(key2);
  }// compareTo

}// Contract
