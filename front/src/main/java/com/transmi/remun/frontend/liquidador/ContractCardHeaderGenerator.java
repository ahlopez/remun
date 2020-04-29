package com.transmi.remun.frontend.liquidador;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.transmi.remun.backend.data.entity.Contract;

public class ContractCardHeaderGenerator
{

  private class HeaderWrapper
  {
    private Predicate<LocalDate> matcher;

    private ContractCardHeader header;

    private Long selected;

    public HeaderWrapper(Predicate<LocalDate> matcher, ContractCardHeader header)
    {
      this.matcher = matcher;
      this.header  = header;
    }

    public boolean matches(LocalDate date) { return matcher.test(date); }

    public Long getSelected() { return selected; }

    public void setSelected(Long selected) { this.selected = selected; }

    public ContractCardHeader getHeader() { return header; }

  }

  private final DateTimeFormatter HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d");

  private final Map<String, ContractCardHeader> contractsWithHeaders = new HashMap<>();

  private List<HeaderWrapper> headerChain = new ArrayList<>();

  private ContractCardHeader getRecentHeader() { return new ContractCardHeader("Reciente", "Anterior a esta semana"); }

  private ContractCardHeader getYesterdayHeader() {
    LocalDate yesterday = LocalDate.now().minusDays(1);
    return new ContractCardHeader("Ayer", secondaryHeaderFor(yesterday));
  }

  private ContractCardHeader getTodayHeader() {
    LocalDate today = LocalDate.now();
    return new ContractCardHeader("Hoy", secondaryHeaderFor(today));
  }

  private ContractCardHeader getThisWeekBeforeYesterdayHeader() {
    LocalDate today         = LocalDate.now();
    LocalDate yesterday     = today.minusDays(1);
    LocalDate thisWeekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
    return new ContractCardHeader("Esta semana antes de ayer", secondaryHeaderFor(thisWeekStart, yesterday));
  }

  private ContractCardHeader getThisWeekStartingTomorrow(boolean showPrevious) {
    LocalDate today         = LocalDate.now();
    LocalDate tomorrow      = today.plusDays(1);
    LocalDate nextWeekStart = today.minusDays(today.getDayOfWeek().getValue()).plusWeeks(1);
    return new ContractCardHeader(
        showPrevious ?
            "Esta semana iniciando ma�ana" :
            "Esta semana",
        secondaryHeaderFor(tomorrow, nextWeekStart)
    );
  }

  private ContractCardHeader getUpcomingHeader() { return new ContractCardHeader("Pr�ximos", "Despu�s de esta semana"); }

  private String secondaryHeaderFor(LocalDate date) { return HEADER_DATE_TIME_FORMATTER.format(date); }

  private String secondaryHeaderFor(LocalDate start, LocalDate end) { return secondaryHeaderFor(start) + " - " + secondaryHeaderFor(end); }

  public ContractCardHeader get(String code) { return contractsWithHeaders.get(code); }

  public void resetHeaderChain(boolean showPrevious) {
    this.headerChain = createHeaderChain(showPrevious);
    contractsWithHeaders.clear();
  }

  public void contractsRead(List<Contract> contracts) {
    Iterator<HeaderWrapper> headerIterator = headerChain.stream().filter(h -> h.getSelected() == null).iterator();
    if (!headerIterator.hasNext())
    { return; }

    HeaderWrapper current = headerIterator.next();
    for (Contract contract : contracts)
    {
      // If last selected, discard contracts that match it.
      if (current.getSelected() != null && current.matches(contract.getFromDate()))
      { continue; }

      while (current != null && !current.matches(contract.getFromDate()))
      { current = headerIterator.hasNext() ?
          headerIterator.next() :
          null; }

      if (current == null)
      { break; }

      current.setSelected(contract.getId());
      contractsWithHeaders.put(contract.getCode(), current.getHeader());
    }

  }

  private List<HeaderWrapper> createHeaderChain(boolean showPrevious) {
    List<HeaderWrapper> headerChain    = new ArrayList<>();
    LocalDate           today          = LocalDate.now();
    LocalDate           startOfTheWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
    if (showPrevious)
    {
      LocalDate yesterday = today.minusDays(1);
      // Week starting on Monday
      headerChain.add(new HeaderWrapper(d -> d.isBefore(startOfTheWeek), this.getRecentHeader()));
      if (startOfTheWeek.isBefore(yesterday))
      { headerChain.add(
          new HeaderWrapper(d -> d.isBefore(yesterday) && !d.isAfter(startOfTheWeek),
              this.getThisWeekBeforeYesterdayHeader()
          )
      ); }

      headerChain.add(new HeaderWrapper(yesterday::equals, this.getYesterdayHeader()));
    }

    LocalDate firstDayOfTheNextWeek = startOfTheWeek.plusDays(7);
    headerChain.add(new HeaderWrapper(today::equals, getTodayHeader()));
    headerChain.add(
        new HeaderWrapper(d -> d.isAfter(today) && d.isBefore(firstDayOfTheNextWeek),
            getThisWeekStartingTomorrow(showPrevious)
        )
    );
    headerChain.add(new HeaderWrapper(d -> !d.isBefore(firstDayOfTheNextWeek), getUpcomingHeader()));
    return headerChain;
  }// createHeaderChain

}// ContractCardHeaderGenerator