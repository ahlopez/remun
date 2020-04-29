package com.transmi.remun.frontend.liquidador.events;

import com.transmi.remun.frontend.liquidador.ContractParmEditor;
import com.vaadin.flow.component.ComponentEvent;

public class CommentChangeEvent extends ComponentEvent<ContractParmEditor>
{

  private final String comment;

  public CommentChangeEvent(ContractParmEditor component, String comment)
  {
    super(component, false);
    this.comment = comment;
  }

  public String getComment() { return comment; }

}// CommentChangeEvent