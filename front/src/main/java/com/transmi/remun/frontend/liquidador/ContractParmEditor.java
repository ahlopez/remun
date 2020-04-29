package com.transmi.remun.frontend.liquidador;

import java.util.Objects;
import java.util.stream.Stream;

import com.transmi.remun.frontend.liquidador.events.CommentChangeEvent;
import com.transmi.remun.frontend.liquidador.events.ContractParmChangeEvent;
import com.transmi.remun.frontend.liquidador.events.DeleteEvent;
import com.transmi.remun.backend.data.entity.Parm;
import com.transmi.remun.service.util.ParmType;
//
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("contract-parm-editor")
@JsModule("./src/views/contractedit/contract-parm-editor.js")
public class ContractParmEditor extends PolymerTemplate<TemplateModel> implements HasValueAndElement<ComponentValueChangeEvent<ContractParmEditor, Parm>, Parm>
{

  @Id("delete")
  private Button delete;

  @Id("name")
  private TextField name;

  @Id("types")
  private ComboBox<String> types = new ComboBox<>();

  @Id("dateFrom")
  private DatePicker dateFrom;

  @Id("dateTo")
  private DatePicker dateTo;

  @Id("value")
  private TextField value;

  @Id("comment")
  private TextField comment;

  private final AbstractFieldSupport<ContractParmEditor, Parm> fieldSupport;

  private BeanValidationBinder<Parm> binder = new BeanValidationBinder<>(Parm.class);

  public ContractParmEditor(DataProvider<Parm, String> contractParmDataProvider)
  {
    this.fieldSupport = new AbstractFieldSupport<>(this, null, Objects::equals, c->
      {});

    binder.bind(name, "name");
    name.setRequiredIndicatorVisible(true);
    name.setRequired(true);

    binder.bind(dateFrom, "dateFrom");
    dateFrom.setRequiredIndicatorVisible(true);
    dateFrom.setRequired(true);

    binder.bind(dateTo, "dateTo");
    dateTo.setRequiredIndicatorVisible(true);
    dateTo.setRequired(true);

    binder.bind(value, "value");
    value.setRequiredIndicatorVisible(true);
    value.setRequired(true);

    binder.bind(comment, "comment");
    comment.setRequiredIndicatorVisible(true);
    comment.setRequired(false);
    comment.addValueChangeListener(e-> fireEvent(new CommentChangeEvent(this, e.getValue())));

    ListDataProvider<String> typeProvider = DataProvider.ofItems(ParmType.getAllTypes());
    types.setItemLabelGenerator(
        s-> s != null ?
            s :
            ""
    );
    types.setDataProvider(typeProvider);
    binder.bind(types, "type");
    types.setRequiredIndicatorVisible(true);
    types.setRequired(false);

    delete.addClickListener(e-> fireEvent(new DeleteEvent(this)));

  }// ContractParmEditor

  @Override
  public Parm getValue() { return fieldSupport.getValue(); }

  @Override
  public void setValue(Parm parm) {
    fieldSupport.setValue(parm);
    binder.setBean(parm);
    boolean noParmSelected = (parm == null || name == null || dateFrom == null || dateTo == null || types == null || value == null);
    value.setEnabled(!noParmSelected);
    delete.setEnabled(!noParmSelected);
    comment.setEnabled(!noParmSelected);
  }// setValue

  public Stream<HasValue<?, ?>> validate() { return binder.validate().getFieldValidationErrors().stream().map(BindingValidationStatus::getField); }

  public Registration addParmChangeListener(ComponentEventListener<ContractParmChangeEvent> listener) { return addListener(ContractParmChangeEvent.class, listener); }

  public Registration addCommentChangeListener(ComponentEventListener<CommentChangeEvent> listener) { return addListener(CommentChangeEvent.class, listener); }

  public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { return addListener(DeleteEvent.class, listener); }

  @Override
  public Registration addValueChangeListener(
    ValueChangeListener<? super ComponentValueChangeEvent<ContractParmEditor, Parm>> listener
  ) { return fieldSupport.addValueChangeListener(listener); }

}// ContractParmEditor
