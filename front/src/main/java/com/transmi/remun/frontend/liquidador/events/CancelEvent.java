package com.transmi.remun.frontend.liquidador.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class CancelEvent extends ComponentEvent<Component>
{
  public CancelEvent(Component source, boolean fromClient)
  { super(source, fromClient); }

}// CancelEvent
