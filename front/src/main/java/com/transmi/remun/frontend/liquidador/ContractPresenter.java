package com.transmi.remun.frontend.liquidador;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.service.ContractService;
import com.transmi.remun.frontend.crud.EntityPresenter;
import com.transmi.remun.frontend.liquidador.ContractsGridDataProvider.ContractFilter;
import com.transmi.remun.frontend.security.CurrentUser;
import com.transmi.remun.service.util.FrontConst;
import com.transmi.remun.service.util.HasLogger;
//
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ContractPresenter implements HasLogger
{

  private ContractCardHeaderGenerator headersGenerator;

  private LiquidadorFrontView view;

  private final EntityPresenter<Contract, LiquidadorFrontView> entityPresenter;

  private final ContractsGridDataProvider dataProvider;

  private final CurrentUser currentUser;

  private final ContractService contractService;

  @Autowired
  ContractPresenter(
      ContractService contractService, ContractsGridDataProvider dataProvider,
      EntityPresenter<Contract, LiquidadorFrontView> entityPresenter, CurrentUser currentUser
  )
  {
    this.contractService = contractService;
    this.entityPresenter = entityPresenter;
    this.dataProvider    = dataProvider;
    this.currentUser     = currentUser;
    headersGenerator     = new ContractCardHeaderGenerator();
    headersGenerator.resetHeaderChain(false);
    dataProvider.setPageObserver(p-> headersGenerator.contractsRead(p.getContent()));
    getLogger().info(">>> Salgo de constructor ContractPresenter");
  }// ContractPresenter

  void init(LiquidadorFrontView view) {
    this.entityPresenter.setView(view);
    this.view = view;
    view.getGrid().setDataProvider(dataProvider);
    view.getOpenedContractEditor().setCurrentUser(currentUser.getUser());
    view.getOpenedContractEditor().addCancelListener(e-> cancel());
    view.getOpenedContractEditor().addReviewListener(e-> review());
    view.getOpenedContractDetails().addSaveListenter(e-> save());
    view.getOpenedContractDetails().addCancelListener(e-> cancel());
    view.getOpenedContractDetails().addBackListener(e-> back());
    view.getOpenedContractDetails().addEditListener(e-> edit());
    view.getOpenedContractDetails().addCommentListener(e-> addComment(e.getMessage()));
  }// init

  ContractCardHeader getHeaderByContractCode(String code) { return headersGenerator.get(code); }

  public void filterChanged(String filter, boolean showPrevious) {
    headersGenerator.resetHeaderChain(showPrevious);
    dataProvider.setFilter(new ContractFilter(filter, showPrevious));
  }// filterChanged

  void onNavigation(Long id, boolean edit) { entityPresenter.loadEntity(id, e-> open(e, edit)); }

  void createNewContract() { open(entityPresenter.createNew(), true); }

  void cancel() { entityPresenter.cancel(()-> close(), ()-> view.setOpened(true)); }

  void closeSilently() {
    entityPresenter.close();
    view.setOpened(false);
  }// closeSilently

  void edit() { UI.getCurrent().navigate(FrontConst.PAGE_LIQUIDADOR_FRONT_EDIT + "/" + entityPresenter.getEntity().getId()); }

  void back() { view.setDialogElementsVisibility(true); }

  void review() {
    getLogger().info(">>> iniciando contractPresenter.review");
    // Using collect instead of findFirst to assure all streams are
    // traversed, and every validation updates its view
    List<HasValue<?, ?>> fields = view.validate().collect(Collectors.toList());
    if (fields.isEmpty())
    {
      if (entityPresenter.writeEntity())
      {
        view.setDialogElementsVisibility(false);
        view.getOpenedContractDetails().display(entityPresenter.getEntity(), true);
      }

    } else if (fields.get(0) instanceof Focusable)
    {
      ((Focusable<?>) fields.get(0)).focus();
    }
    getLogger().info(">>> saliendo de contractPresenter.review");

  }// review

  void save() {
    getLogger().info(">>> iniciando contractPresenter.save()");
    entityPresenter.save(e->
      {
        if (entityPresenter.isNew())
        {
          view.showCreatedNotification();
          dataProvider.refreshAll();
        } else
        {
          view.showUpdatedNotification();
          dataProvider.refreshItem(e);
        }

        close();
      });
    getLogger().info(">>> saliendo contractPresenter.save()");

  }// save

  void addComment(String comment) {
    if (entityPresenter.executeUpdate(e-> contractService.addComment(currentUser.getUser(), e, comment)))
    {
      // You can only add comments when in view mode, so reopening in that state.
      open(entityPresenter.getEntity(), false);
    }

  }// addComment

  private void open(Contract contract, boolean edit) {
    view.setDialogElementsVisibility(edit);
    view.setOpened(true);

    if (edit)
      view.getOpenedContractEditor().read(contract, entityPresenter.isNew());
    else
      view.getOpenedContractDetails().display(contract, false);

  }// open

  private void close() {
    getLogger().info(">>> entrando contractPresenter.close()");
    view.getOpenedContractEditor().close();
    view.setOpened(false);
    view.navigateToMainView();
    entityPresenter.close();
    getLogger().info(">>> saliendo contractPresenter.close()");

  }// close

}// ContractPresenter
