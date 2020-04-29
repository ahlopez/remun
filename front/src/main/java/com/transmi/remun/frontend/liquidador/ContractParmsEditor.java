package com.transmi.remun.frontend.liquidador;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.transmi.remun.backend.data.entity.Parm;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.shared.Registration;

public class ContractParmsEditor extends Div implements HasValueAndElement<ComponentValueChangeEvent<ContractParmsEditor, List<Parm>>, List<Parm>>
{

  private ContractParmEditor empty;

  private DataProvider<Parm, String> productDataProvider;

  private boolean hasChanges = false;

  private final AbstractFieldSupport<ContractParmsEditor, List<Parm>> fieldSupport;

  public ContractParmsEditor(DataProvider<Parm, String> productDataProvider)
  {
    this.productDataProvider = productDataProvider;
    this.fieldSupport        = new AbstractFieldSupport<>(
        this, Collections.emptyList(),
        Objects::equals, c->
                                   {}
    );
  }

  @Override
  public void setValue(List<Parm> parms) {
    fieldSupport.setValue(parms);
    removeAll();
    hasChanges = false;

    if (parms != null)
    {
      parms.forEach(this::createEditor);
    }

    createEmptyElement();
    setHasChanges(false);
  }

  private ContractParmEditor createEditor(Parm value) {
    ContractParmEditor editor = new ContractParmEditor(productDataProvider);
    getElement().appendChild(editor.getElement());
    editor.addParmChangeListener(e-> parmChanged(e.getSource(), e.getParm()));
    editor.addCommentChangeListener(e-> setHasChanges(true));
    editor.addDeleteListener(e->
      {
        ContractParmEditor contractParmEditor = e.getSource();
        if (contractParmEditor != empty)
        {
          remove(contractParmEditor);
          Parm contractParm = contractParmEditor.getValue();
          setValue(getValue().stream().filter(element-> element != contractParm).collect(Collectors.toList()));
          setHasChanges(true);
        }

      });

    editor.setValue(value);
    return editor;
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    HasValueAndElement.super.setReadOnly(readOnly);
    getChildren().forEach(e-> ((ContractParmEditor) e).setReadOnly(readOnly));
  }

  @Override
  public List<Parm> getValue() { return fieldSupport.getValue(); }

  private void parmChanged(ContractParmEditor parm, Parm contractParm) {
    setHasChanges(true);
    if (empty == parm)
    {
      createEmptyElement();
      Parm newParm = contractParm.createParm();
      parm.setValue(newParm);
      setValue(Stream.concat(getValue().stream(), Stream.of(contractParm)).collect(Collectors.toList()));
    }

  }

  private void createEmptyElement() { empty = createEditor(null); }

  public boolean hasChanges() { return hasChanges; }

  private void setHasChanges(boolean hasChanges) {
    this.hasChanges = hasChanges;
    if (hasChanges)
    {
      fireEvent(new com.transmi.remun.frontend.liquidador.events.ValueChangeEvent(this));
    }

  }

  public Stream<HasValue<?, ?>> validate() { return getChildren()
      .filter(component-> fieldSupport.getValue().size() == 0 || !component.equals(empty))
      .map(editor-> ((ContractParmEditor) editor).validate()).flatMap(stream-> stream); }

  @Override
  public Registration addValueChangeListener(
    ValueChangeListener<? super ComponentValueChangeEvent<ContractParmsEditor, List<Parm>>> listener
  ) { return fieldSupport.addValueChangeListener(listener); }

}// ContractParmsEditor
