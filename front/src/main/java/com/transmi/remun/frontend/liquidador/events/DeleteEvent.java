package com.transmi.remun.frontend.liquidador.events;

import com.transmi.remun.frontend.liquidador.ContractParmEditor;
import com.vaadin.flow.component.ComponentEvent;

public class DeleteEvent extends ComponentEvent<ContractParmEditor> {
   public DeleteEvent(ContractParmEditor component) {
      super(component, false);
   }
}//DeleteEvent
