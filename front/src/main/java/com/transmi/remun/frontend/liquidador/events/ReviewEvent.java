package com.transmi.remun.frontend.liquidador.events;

import com.transmi.remun.frontend.liquidador.ContractEditor;
import com.vaadin.flow.component.ComponentEvent;

public class ReviewEvent extends ComponentEvent<ContractEditor>
{

  public ReviewEvent(ContractEditor component)
  { super(component, false); }

}// ReviewEvent