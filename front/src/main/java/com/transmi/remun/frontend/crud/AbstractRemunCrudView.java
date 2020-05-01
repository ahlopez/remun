package com.transmi.remun.frontend.crud;

import java.util.function.Consumer;

import com.transmi.remun.backend.data.entity.AbstractEntity;
import com.transmi.remun.backend.data.entity.EntityUtil;
import com.transmi.remun.backend.service.FilterableCrudService;
import com.transmi.remun.frontend.components.HasNotifications;
import com.transmi.remun.frontend.components.SearchBar;
import com.transmi.remun.frontend.security.CurrentUser;
import com.transmi.remun.service.util.TemplateUtil;
//
import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;

public abstract class AbstractRemunCrudView<E extends AbstractEntity> extends Crud<E>
    implements HasUrlParameter<Long>, HasNotifications
{

  private static final String DISCARD_MESSAGE = "Hay cambios sin guardar al %s. Descarta los cambios?";
  private static final String DELETE_MESSAGE = "Está seguro de que quiere eliminar el %s seleccionado? Esta acción no puede deshacerse";

//  private final Grid<E> grid;
  private final CrudEntityPresenter<E> entityPresenter;

  protected abstract String getBasePage();

  protected abstract void setupGrid(Grid<E> grid);

  public AbstractRemunCrudView(
      Class<E> beanType,
      FilterableCrudService<E> service,
      Grid<E> grid,
      CrudEditor<E> editor,
      CurrentUser currentUser
  )
  {
    super(beanType, grid, editor);
//    this.grid = grid;
    grid.setSelectionMode(Grid.SelectionMode.NONE);
    CrudI18n crudI18n   = CrudI18n.createDefault();
    String   entityName = EntityUtil.getName(beanType);
    crudI18n.setNewItem("Crear " + entityName);
    crudI18n.setEditItem("Editar " + entityName);
    crudI18n.setEditLabel("Editar " + entityName);
    crudI18n.getConfirm().getCancel().setContent(String.format(DISCARD_MESSAGE, entityName));
    crudI18n.getConfirm().getDelete().setContent(String.format(DELETE_MESSAGE, entityName));
    crudI18n.setDeleteItem("Eliminar");
    setI18n(crudI18n);
    CrudEntityDataProvider<E> dataProvider = new CrudEntityDataProvider<>(service);
    grid.setDataProvider(dataProvider);
    setupGrid(grid);
    Crud.addEditColumn(grid);
    entityPresenter = new CrudEntityPresenter<>(service, currentUser, this);
    SearchBar searchBar = new SearchBar();
    searchBar.setActionText("Crear " + entityName);
    searchBar.setPlaceHolder("Buscar");
    searchBar.addFilterChangeListener(e-> dataProvider.setFilter(searchBar.getFilter()));
    searchBar.getActionButton().getElement().setAttribute("new-button", true);
    setToolbar(searchBar);
    setupCrudEventListeners(entityPresenter);
  }// AbstractRemunCrudView

  private void setupCrudEventListeners(CrudEntityPresenter<E> entityPresenter) {
    Consumer<E> onSuccess = entity-> navigateToEntity(null);
    Consumer<E> onFail    = entity->
                            {
                              throw new RuntimeException("La operación no pudo ser ejecutada.");
                            };
    addEditListener(
        e-> entityPresenter.loadEntity(
            e.getItem().getId(),
            entity-> navigateToEntity(entity.getId().toString())
        )
    );
    addCancelListener(e-> navigateToEntity(null));
    addSaveListener(e-> entityPresenter.save(e.getItem(), onSuccess, onFail));
    addDeleteListener(e-> entityPresenter.delete(e.getItem(), onSuccess, onFail));
  }// setupCrudEventListeners

  protected void navigateToEntity(String id) { getUI().ifPresent(ui-> ui.navigate(TemplateUtil.generateLocation(getBasePage(), id))); }

  @Override
  public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
    if (id != null)
    {
      E item = getEditor().getItem();
      if (item != null && id.equals(item.getId()))
      {
        return;
      }
      entityPresenter.loadEntity(id, entity-> edit(entity, EditMode.EXISTING_ITEM));
    } else
    {
      setOpened(false);
    }
  }// setParameter

}// AbstractRemunCrudView
