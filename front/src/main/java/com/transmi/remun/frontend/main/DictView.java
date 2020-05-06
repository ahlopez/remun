package com.transmi.remun.frontend.main;

import static com.transmi.remun.service.util.FrontConst.PAGE_DICT;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.service.ContractService;
import com.transmi.remun.frontend.crud.AbstractRemunCrudView;
import com.transmi.remun.frontend.security.CurrentUser;
import com.transmi.remun.service.util.ContractStatus;
import com.transmi.remun.service.util.FrontConst;
import com.transmi.remun.service.util.HasLogger;
import com.transmi.remun.service.util.Role;
import com.transmi.remun.service.util.TransmiPhase;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = PAGE_DICT, layout = MainView.class)
@PageTitle(FrontConst.TITLE_DICT)
@Secured(Role.ADMIN)
public class DictView extends AbstractRemunCrudView<Contract> implements HasLogger
{

  private static Logger log;

  @Autowired
  public DictView(ContractService service, CurrentUser currentUser, PasswordEncoder passwordEncoder)
  { super(
      Contract.class,
      service,
      new Grid<>(),
      createEditor(passwordEncoder),
      currentUser
  ); }

  @Override
  public void setupGrid(Grid<Contract> grid) {
    grid.addColumn(Contract::getFase).setWidth("85px").setHeader("Fase").setFlexGrow(5);
    grid.addColumn(Contract::getCode).setWidth("120px").setHeader("Código").setFlexGrow(5);
    grid.addColumn(Contract::getName).setWidth("200px").setHeader("Nombre").setFlexGrow(5);
    grid.addColumn(
        c-> c.getContractor() == null ?
            "-" :
            c.getContractor().getFullName()
    ).setWidth("200px").setHeader("Contratista").setFlexGrow(5);
    grid.addColumn(Contract::getFromDate).setWidth("115px").setHeader("Desde").setFlexGrow(5);
    grid.addColumn(Contract::getToDate).setWidth("115px").setHeader("Hasta").setFlexGrow(5);
    grid.addColumn(
        c-> c.isVigente() ?
            "SI" :
            "NO"
    ).setHeader("Vigente").setWidth("85px").setFlexGrow(5);
    grid.getColumns().forEach(col-> col.setAutoWidth(true));
  }// setupGrid

  @Override
  protected String getBasePage() { return PAGE_DICT; }

  private static BinderCrudEditor<Contract> createEditor(PasswordEncoder passwordEncoder) {

    Notification.show("Saludos desde Diccionario!!");
    log = Logger.getGlobal();
    log.info(">>> Entrando a createEditor");
    ComboBox<TransmiPhase> fase = new ComboBox<>();
    fase.getElement().setAttribute("colspan", "1");
    fase.setLabel("Fase");
    log.info(">>> Cree combo fase");

    TextField  code       = new TextField("Código");
    TextField  name       = new TextField("Nombre");
    TextField  contractor = new TextField("Contratista");
    DatePicker fromDate   = new DatePicker("Desde");
    DatePicker toDate     = new DatePicker("Hasta");

    ComboBox<ContractStatus> status = new ComboBox<>();
    status.getElement().setAttribute("colspan", "1");
    status.setLabel("Estado");
    log.info(">>> Cree combo status");

    FormLayout form = new FormLayout(fase, code, name, contractor, fromDate, toDate, status);

    BeanValidationBinder<Contract> binder = new BeanValidationBinder<>(Contract.class);

    ListDataProvider<ContractStatus> statusProvider = DataProvider.ofItems(ContractStatus.getAllStatus());
    status.setItemLabelGenerator(
        s-> s != null ?
            s.toString() :
            ""
    );
    log.info(">>> Cree status provider");
    status.setDataProvider(statusProvider);
    log.info(">>> status provider assigned");
    ListDataProvider<TransmiPhase> phaseProvider = DataProvider.ofItems(TransmiPhase.getAllPhases());
    fase.setItemLabelGenerator(
        f-> f != null ?
            f.toString() :
            ""
    );
    log.info(">>> Cree phase provider");
    fase.setDataProvider(phaseProvider);
    log.info(">>> fase provider assigned");

    binder.bind(fase, "fase");
    binder.bind(code, "code");
    binder.bind(name, "name");

    binder.bind(contractor, "contractor");
    binder.bind(fromDate, "fromDate");
    binder.bind(toDate, "toDate");
    binder.bind(status, "status");

    log.info(">>> Cree contract binder crud editor");

    return new BinderCrudEditor<Contract>(binder, form);
  }// createEditor

}// DictView
