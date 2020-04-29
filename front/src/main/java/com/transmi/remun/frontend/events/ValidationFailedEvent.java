package com.transmi.remun.frontend.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class ValidationFailedEvent extends ComponentEvent<Component> {

   public ValidationFailedEvent(Component source) {
      super(source, false);
   }

}//ValidationFailedEvent