package com.transmi.remun.frontend.crud;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import com.transmi.remun.backend.data.entity.AbstractEntity;
import com.transmi.remun.backend.service.FilterableCrudService;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;

public class CrudEntityDataProvider<T extends AbstractEntity> extends FilterablePageableDataProvider<T, String>
{

  private final FilterableCrudService<T> crudService;

  private List<QuerySortOrder> defaultSortOrders;

  public CrudEntityDataProvider(FilterableCrudService<T> crudService)
  {
    this.crudService = crudService;
    setSortOrders();
  }// CrudEntityDataProvider

  private void setSortOrders() {
    QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
    builder.thenAsc("id");
    defaultSortOrders = builder.build();
  }// setSortOrders

  @Override
  protected Page<T> fetchFromBackEnd(Query<T, String> query, Pageable pageable) { return crudService.findAnyMatching(query.getFilter(), pageable); }

  @Override
  protected List<QuerySortOrder> getDefaultSortOrders() { return defaultSortOrders; }

  @Override
  protected int sizeInBackEnd(Query<T, String> query) { return (int) crudService.countAnyMatching(query.getFilter()); }

}// CrudEntityDataProvider
