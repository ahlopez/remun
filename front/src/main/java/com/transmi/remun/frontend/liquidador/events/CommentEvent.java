package com.transmi.remun.frontend.liquidador.events;

import com.transmi.remun.frontend.liquidador.ContractDetails;
import com.vaadin.flow.component.ComponentEvent;

public class CommentEvent extends ComponentEvent<ContractDetails>
{

  private String contractCode;

  private String message;

  public CommentEvent(ContractDetails component, String contractCode, String message)
  {
    super(component, false);
    this.contractCode = contractCode;
    this.message      = message;
  }

  public String getContractCode() { return contractCode; }

  public String getMessage() { return message; }

}// CommentEvent