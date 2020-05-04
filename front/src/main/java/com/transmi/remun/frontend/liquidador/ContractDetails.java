package com.transmi.remun.frontend.liquidador;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.service.ContractorService;
import com.transmi.remun.frontend.components.converters.ContractStateConverter;
import com.transmi.remun.frontend.components.converters.LocalDateConverter;
import com.transmi.remun.frontend.components.converters.LocalDateTimeConverter;
import com.transmi.remun.frontend.components.converters.PhaseConverter;
import com.transmi.remun.frontend.liquidador.events.CancelEvent;
import com.transmi.remun.frontend.liquidador.events.CommentEvent;
import com.transmi.remun.frontend.liquidador.events.EditEvent;
import com.transmi.remun.frontend.liquidador.events.SaveEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.Include;
import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * The component displaying a full (read-only) summary of an Contract, and a comment
 * field to add comments.
 */
@Tag("contract-details")
@JsModule("./src/views/contractedit/contract-details.js")
public class ContractDetails extends PolymerTemplate<ContractDetails.Model>
{

  private Contract contract;

  @Id("back")
  private Button back;

  @Id("cancel")
  private Button cancel;

  @Id("save")
  private Button save;

  @Id("edit")
  private Button edit;

  @Id("history")
  private Element history;

  @Id("comment")
  private Element comment;

  @Id("sendComment")
  private Button sendComment;

  @Id("commentField")
  private TextField commentField;

  private boolean isDirty;

  public ContractDetails()
  {
    sendComment.addClickListener(e->
      {
        String message = commentField.getValue();
        message = message == null ?
            "" :
            message.trim();
        if (!message.isEmpty())
        {
          commentField.clear();
          fireEvent(new CommentEvent(this, contract.getCode(), message));
        }

      }
    );
    save.addClickListener(e->
      {
        contract.setContractor(ContractorService.getGlobalContractor());
        fireEvent(new SaveEvent(this, false));
      });
    cancel.addClickListener(e-> fireEvent(new CancelEvent(this, false)));
    edit.addClickListener(e-> fireEvent(new EditEvent(this)));
  }

  public void display(Contract contract, boolean review) {
    getModel().setReview(review);
    this.contract = contract;
    getModel().setItem(contract);
    if (!review)
    {
      commentField.clear();
    }

    this.isDirty = review;
  }

  public boolean isDirty() { return isDirty; }

  public void setDirty(boolean isDirty) { this.isDirty = isDirty; }

  public interface Model extends TemplateModel
  {

    @Include({
        "fase",
        "code",
        "status",
        "name",
        // "contractor",
        "fromDate",
        "toDate",
        "contract.details",
        "parms.code",
        "parms.value",
        "history.message",
        "history.createdBy.firstName",
        "history.timestamp",
        "history.newState"
    })
    // @Encode(value = LongToStringConverter.class, path = "code")
    @Encode(value = PhaseConverter.class, path = "fase")
    @Encode(value = LocalDateConverter.class, path = "fromDate")
    @Encode(value = LocalDateConverter.class, path = "toDate")
    @Encode(value = ContractStateConverter.class, path = "status")
    @Encode(value = LocalDateTimeConverter.class, path = "history.timestamp")
    @Encode(value = ContractStateConverter.class, path = "history.newState")
    void setItem(Contract contract);

    void setReview(boolean review);

  }

  public Registration addSaveListenter(ComponentEventListener<SaveEvent> listener) { return addListener(SaveEvent.class, listener); }

  public Registration addEditListener(ComponentEventListener<EditEvent> listener) { return addListener(EditEvent.class, listener); }

  public Registration addBackListener(ComponentEventListener<ClickEvent<Button>> listener) { return back.addClickListener(listener); }

  public Registration addCommentListener(ComponentEventListener<CommentEvent> listener) { return addListener(CommentEvent.class, listener); }

  public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) { return addListener(CancelEvent.class, listener); }

}// ContractDetails
