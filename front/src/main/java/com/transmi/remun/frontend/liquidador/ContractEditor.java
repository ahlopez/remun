package com.transmi.remun.frontend.liquidador;

import static com.transmi.remun.frontend.components.DataProviderUtil.createItemLabelGenerator;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.data.entity.Parm;
import com.transmi.remun.backend.data.entity.User;
import com.transmi.remun.backend.service.ParmService;
import com.transmi.remun.frontend.components.DataProviderUtil;
import com.transmi.remun.frontend.crud.CrudEntityDataProvider;
import com.transmi.remun.frontend.liquidador.events.CancelEvent;
import com.transmi.remun.frontend.liquidador.events.ReviewEvent;
import com.transmi.remun.frontend.liquidador.events.ValueChangeEvent;
import com.transmi.remun.service.util.ContractStatus;
import com.transmi.remun.service.util.TransmiPhase;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.validator.BeanValidator;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("contract-editor")
@JsModule("./src/views/contractedit/contract-editor.js")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ContractEditor extends PolymerTemplate<ContractEditor.Model>
{

  public interface Model extends TemplateModel
  {

    void setStatus(String status);

  }

  @Id("title")
  private H2 title;

  @Id("metaContainer")
  private Div metaContainer;

  @Id("fase")
  private ComboBox<TransmiPhase> fase;

  @Id("code")
  private Span code;

  @Id("contractor")
  private TextField contractorName;

  @Id("name")
  private TextField name;

  @Id("fromDate")
  private DatePicker fromDate;

  @Id("toDate")
  private DatePicker toDate;

  @Id("status")
  private ComboBox<ContractStatus> status;

  @Id("cancel")
  private Button cancel;

  @Id("review")
  private Button review;

  @Id("parmsContainer")
  private Div parmsContainer;

  private ContractParmsEditor parmsEditor;

  private User currentUser;

  private BeanValidationBinder<Contract> binder = new BeanValidationBinder<>(Contract.class);

  @Autowired
  public ContractEditor(ParmService parmService)
  {
    DataProvider<Parm, String> parmDataProvider = new CrudEntityDataProvider<>(parmService);
    parmsEditor = new ContractParmsEditor(parmDataProvider);

    parmsContainer.add(parmsEditor);

    cancel.addClickListener(e-> fireEvent(new CancelEvent(this, false)));
    review.addClickListener(e-> fireEvent(new ReviewEvent(this)));

    status.setItemLabelGenerator(createItemLabelGenerator(ContractStatus::getDisplayName));
    status.setDataProvider(DataProvider.ofItems(ContractStatus.values()));
    status.addValueChangeListener(
        e-> getModel().setStatus(DataProviderUtil.convertIfNotNull(e.getValue(), ContractStatus::name))
    );
    binder.forField(status)
        .withValidator(new BeanValidator(Contract.class, "status"))
        .bind(
            Contract::getStatus, (
              o, s)->
              {
                o.changeState(currentUser, s);
              }
        );

    fase.setRequired(true);
    binder.bind(fase, "fase");

    fromDate.setRequired(true);
    binder.bind(fromDate, "fromDate");

    toDate.setRequired(true);
    binder.bind(toDate, "toDate");

    contractorName.setRequired(true);
    binder.bind(contractorName, "contractor.fullName");

    parmsEditor.setRequiredIndicatorVisible(true);
    binder.bind(parmsEditor, "parms");

    ComponentUtil.addListener(parmsEditor, ValueChangeEvent.class, e-> review.setEnabled(hasChanges()));
    binder.addValueChangeListener(e->
      {
        if (e.getOldValue() != null)
        {
          review.setEnabled(hasChanges());
        }

      });
  }

  public boolean hasChanges() { return binder.hasChanges() || parmsEditor.hasChanges(); }

  public void clear() {
    binder.readBean(null);
    parmsEditor.setValue(null);
  }

  public void write(Contract contract) throws ValidationException { binder.writeBean(contract); }

  public void read(Contract contract, boolean isNew) {
    binder.readBean(contract);

    this.code.setText(
        isNew ?
            "" :
            contract.getCode()
    );
    title.setVisible(isNew);
    metaContainer.setVisible(!isNew);

    if (contract.getStatus() != null)
    {
      getModel().setStatus(contract.getStatus().name());
    }

    review.setEnabled(false);
  }// read

  public void close() {}

  public Stream<HasValue<?, ?>> validate() {
    Stream<HasValue<?, ?>> errorFields = binder.validate().getFieldValidationErrors().stream()
        .map(BindingValidationStatus::getField);

    return Stream.concat(errorFields, parmsEditor.validate());
  }

  public Registration addReviewListener(ComponentEventListener<ReviewEvent> listener) { return addListener(ReviewEvent.class, listener); }

  public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) { return addListener(CancelEvent.class, listener); }

  public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }

}// ContractEditor
