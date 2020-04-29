package com.transmi.remun.frontend.liquidador;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.service.ContractService;
import com.transmi.remun.service.util.FrontConst;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.QuerySortOrderBuilder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * A pageable contract data provider.
 */
@SpringComponent
@UIScope
public class ContractsGridDataProvider extends FilterablePageableDataProvider<Contract, ContractsGridDataProvider.ContractFilter>
{

  public static class ContractFilter implements Serializable
  {

    private String filter;

    private boolean showPrevious;

    public String getFilter() { return filter; }

    public boolean isShowPrevious() { return showPrevious; }

    public ContractFilter(String filter, boolean showPrevious)
    {
      this.filter       = filter;
      this.showPrevious = showPrevious;
    }

    public static ContractFilter getEmptyFilter() { return new ContractFilter("", false); }

  }// ContractFilter

  private final ContractService contractService;

  private List<QuerySortOrder> defaultSortOrders;

  private Consumer<Page<Contract>> pageObserver;

  @Autowired
  public ContractsGridDataProvider(ContractService contractService)
  {
    this.contractService = contractService;
    setSortOrders(FrontConst.DEFAULT_SORT_DIRECTION, FrontConst.ORDER_SORT_FIELDS);
  }

  private void setSortOrders(Sort.Direction direction, String[] properties) {
    QuerySortOrderBuilder builder = new QuerySortOrderBuilder();
    for (String property : properties)
    {
      if (direction.isAscending())
      {
        builder.thenAsc(property);
      } else
      {
        builder.thenDesc(property);
      }

    }

    defaultSortOrders = builder.build();
  }

  @Override
  protected Page<Contract> fetchFromBackEnd(Query<Contract, ContractFilter> query, Pageable pageable) {
    ContractFilter filter = query.getFilter().orElse(ContractFilter.getEmptyFilter());
    Page<Contract> page   = contractService.findAnyMatching(
        Optional.ofNullable(filter.getFilter()),
        pageable
    );
    if (pageObserver != null)
    {
      pageObserver.accept(page);
    }

    return page;
  }

  @Override
  protected List<QuerySortOrder> getDefaultSortOrders() { return defaultSortOrders; }

  @Override
  protected int sizeInBackEnd(Query<Contract, ContractFilter> query) { return (int) contractService.count(); }

  public void setPageObserver(Consumer<Page<Contract>> pageObserver) { this.pageObserver = pageObserver; }

  @Override
  public Object getId(Contract item) { return item.getId(); }

}
