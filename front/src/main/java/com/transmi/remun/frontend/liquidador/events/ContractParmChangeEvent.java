package com.transmi.remun.frontend.liquidador.events;

import com.transmi.remun.frontend.liquidador.ContractParmEditor;
import com.transmi.remun.backend.data.entity.Parm;
import com.vaadin.flow.component.ComponentEvent;

public class ContractParmChangeEvent extends ComponentEvent<ContractParmEditor>
{

  private final Parm parm;

  public ContractParmChangeEvent(ContractParmEditor component, Parm parm)
  {
    super(component, false);
    this.parm = parm;
  }// ParmChangeEvent

  public Parm getParm() { return parm; }

}// ContractParmChangeEvent