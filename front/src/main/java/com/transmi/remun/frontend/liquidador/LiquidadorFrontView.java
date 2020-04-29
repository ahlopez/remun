package com.transmi.remun.frontend.liquidador;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.data.entity.EntityUtil;
import com.transmi.remun.frontend.components.SearchBar;
import com.transmi.remun.frontend.main.EntityView;
import com.transmi.remun.frontend.main.MainView;
import com.transmi.remun.service.util.FrontConst;
import com.transmi.remun.service.util.HasLogger;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("storefront-view")
@JsModule("./src/views/storefront/storefront-view.js")
@Route(value = FrontConst.PAGE_LIQUIDADOR_FRONT, layout = MainView.class)
@RouteAlias(value = FrontConst.PAGE_LIQUIDADOR_FRONT_EDIT, layout = MainView.class)
@RouteAlias(value = FrontConst.PAGE_ROOT, layout = MainView.class)
@PageTitle(FrontConst.TITLE_LIQUIDADOR_FRONT)
public class LiquidadorFrontView extends PolymerTemplate<TemplateModel>
    implements HasLogger, HasUrlParameter<Long>, EntityView<Contract>
{

  @Id("search")
  private SearchBar searchBar;

  @Id("grid")
  private Grid<Contract> grid;

  @Id("dialog")
  private Dialog dialog;

  private ConfirmDialog confirmation;

  private final ContractEditor contractEditor;

  private final ContractDetails contractDetails = new ContractDetails();

  private final ContractPresenter presenter;

  @Autowired
  public LiquidadorFrontView(ContractPresenter presenter, ContractEditor contractEditor)
  {
    this.presenter      = presenter;
    this.contractEditor = contractEditor;
    searchBar.setActionText("Administracion");
    searchBar.setActionText("Diccionario");
    searchBar.setActionText("Fórmulación ");
    searchBar.setPlaceHolder("Liquidación");
    searchBar.setCheckboxText("Buscar");
    grid.setSelectionMode(Grid.SelectionMode.NONE);
    grid.addColumn(
        ContractCard.getTemplate()
            .withProperty("contractCard", ContractCard::create)
            .withProperty("header", contract-> presenter.getHeaderByContractCode(contract.getCode()))
            .withEventHandler("cardClick", contract-> UI.getCurrent().navigate(FrontConst.PAGE_LIQUIDADOR_FRONT + "/" + contract.getCode()))
    );

    getSearchBar().addFilterChangeListener(
        e-> presenter.filterChanged(getSearchBar().getFilter(), getSearchBar().isCheckboxChecked())
    );
    getSearchBar().addActionClickListener(e-> presenter.createNewContract());
    presenter.init(this);
    dialog.addDialogCloseActionListener(e-> presenter.cancel());
  }

  @Override
  public ConfirmDialog getConfirmDialog() { return confirmation; }

  @Override
  public void setConfirmDialog(ConfirmDialog confirmDialog) { this.confirmation = confirmDialog; }

  void setOpened(boolean opened) { dialog.setOpened(opened); }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter Long contractCode) {
    boolean editView = event.getLocation().getPath().contains(FrontConst.PAGE_LIQUIDADOR_FRONT_EDIT);
    if (contractCode != null)
    {
      presenter.onNavigation(contractCode, editView);
    } else if (dialog.isOpened())
    {
      presenter.closeSilently();
    }

  }

  void navigateToMainView() { getUI().ifPresent(ui-> ui.navigate(FrontConst.PAGE_LIQUIDADOR_FRONT)); }

  @Override
  public boolean isDirty() { return contractEditor.hasChanges() || contractDetails.isDirty(); }

  @Override
  public void write(Contract entity) throws ValidationException { contractEditor.write(entity); }

  public Stream<HasValue<?, ?>> validate() { return contractEditor.validate(); }

  SearchBar getSearchBar() { return searchBar; }

  ContractEditor getOpenedContractEditor() { return contractEditor; }

  ContractDetails getOpenedContractDetails() { return contractDetails; }

  Grid<Contract> getGrid() { return grid; }

  @Override
  public void clear() {
    contractDetails.setDirty(false);
    contractEditor.clear();
  }

  void setDialogElementsVisibility(boolean editing) {
    dialog.add(
        editing ?
            contractEditor :
            contractDetails
    );
    contractEditor.setVisible(editing);
    contractDetails.setVisible(!editing);
  }

  @Override
  public String getEntityName() { return EntityUtil.getName(Contract.class); }

}// LiquidadorFrontView
