package com.transmi.remun.frontend.liquidador.events;

import com.transmi.remun.frontend.liquidador.ContractParmsEditor;
import com.vaadin.flow.component.ComponentEvent;

public class ValueChangeEvent extends ComponentEvent<ContractParmsEditor>
{

  public ValueChangeEvent(ContractParmsEditor component)
  { super(component, false); }

}// ValueChangeEvent