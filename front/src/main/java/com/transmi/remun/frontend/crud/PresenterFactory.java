/**
 *
 */
package com.transmi.remun.frontend.crud;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.transmi.remun.backend.data.entity.Contract;
import com.transmi.remun.backend.service.ContractService;
import com.transmi.remun.frontend.liquidador.LiquidadorFrontView;
import com.transmi.remun.frontend.security.CurrentUser;

@Configuration
public class PresenterFactory
{

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public EntityPresenter<Contract, LiquidadorFrontView> orderEntityPresenter(ContractService crudService, CurrentUser currentUser) { return new EntityPresenter<>(crudService, currentUser); }

}
