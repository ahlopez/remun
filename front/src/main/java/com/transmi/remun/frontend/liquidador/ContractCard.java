package com.transmi.remun.frontend.liquidador;

import static com.transmi.remun.service.util.FormattingUtils.FULL_DATE_FORMATTER;

import java.util.List;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.data.entity.Parm;
import com.transmi.remun.frontend.components.converters.ContractStateConverter;
import com.vaadin.flow.data.renderer.TemplateRenderer;

/**
 * Help class to get ready to use TemplateRenderer for displaying order card list on the Storefront and Dashboard grids.
 * Using TemplateRenderer instead of ComponentRenderer optimizes the CPU and memory consumption.
 * <p>
 * In addition, component includes an optional header above the order card. It is used
 * to visually separate orders into groups. Technically all order cards are
 * equivalent, but those that do have the header visible create a visual group
 * separation.
 */
public class ContractCard
{

  public static TemplateRenderer<Contract> getTemplate() { return TemplateRenderer.of(
      "<contract-card"
          + "  header='[[item.header]]'"
          + "  contract-card='[[item.contractCard]]'"
          + "  on-card-click='cardClick'>"
          + "</contract-card>"
  ); }

  public static ContractCard create(Contract contract) { return new ContractCard(contract); }

  private static ContractStateConverter stateConverter = new ContractStateConverter();

  private final Contract contract;

  public ContractCard(Contract contract)
  { this.contract = contract; }

  public String getFase() { return contract.getFase().toString(); }

  public String getCode() { return contract.getCode(); }

  public String getName() { return contract.getName(); }

  public String getContractor() { return contract.getContractor().getFullName(); }

  public String getFromDate() { return FULL_DATE_FORMATTER.format(contract.getFromDate()); }

  public String getToDate() { return FULL_DATE_FORMATTER.format(contract.getToDate()); }

  public String getState() { return stateConverter.encode(contract.getState()); }

  public List<Parm> getParms() { return contract.getParms(); }

}// ContractCard
