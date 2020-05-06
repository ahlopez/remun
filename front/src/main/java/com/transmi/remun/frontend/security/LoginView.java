package com.transmi.remun.frontend.security;

import com.transmi.remun.frontend.liquidador.LiquidadorFrontView;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route
@PageTitle("Remuneracion Agentes")
@JsModule("./styles/shared-styles.js")
public class LoginView extends LoginOverlay
    implements AfterNavigationObserver, BeforeEnterObserver
{

  public LoginView()
  {
    LoginI18n i18n = LoginI18n.createDefault();
    i18n.setHeader(new LoginI18n.Header());
    i18n.getHeader().setTitle("Remuneración de Agentes");
    i18n.getHeader().setDescription(
        "admin@vaadin.com + admin\n" + "barista@vaadin.com + barista"
    );
    i18n.setAdditionalInformation(null);
    i18n.setForm(new LoginI18n.Form());
    i18n.getForm().setSubmit("Sign in");
    i18n.getForm().setTitle("Ingresar");
    i18n.getForm().setUsername("Correo electrónico");
    i18n.getForm().setPassword("Clave");
    setI18n(i18n);
    setForgotPasswordButtonVisible(false);
    setAction("login");
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (SecurityUtils.isUserLoggedIn())
    {
      event.forwardTo(LiquidadorFrontView.class);
    } else
    {
      setOpened(true);
    }
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) { setError(
      event.getLocation().getQueryParameters().getParameters().containsKey(
          "error"
      )
  ); }

}// LoginView
