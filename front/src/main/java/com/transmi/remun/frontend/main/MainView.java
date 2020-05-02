package com.transmi.remun.frontend.main;

import static com.transmi.remun.service.util.FrontConst.TITLE_ADMINISTRACION;
import static com.transmi.remun.service.util.FrontConst.TITLE_DICT;
import static com.transmi.remun.service.util.FrontConst.TITLE_FORMULACION;
import static com.transmi.remun.service.util.FrontConst.TITLE_LIQUIDACION;
import static com.transmi.remun.service.util.FrontConst.TITLE_LOGOUT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.transmi.remun.frontend.components.HasConfirmation;
import com.transmi.remun.frontend.liquidador.LiquidadorFrontView;
import com.transmi.remun.frontend.security.SecurityUtils;
import com.transmi.remun.frontend.security.UsersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServlet;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and
 * use @Route annotation to announce it in a URL as a Spring managed
 * bean.
 * Use the @PWA annotation make the application installable on phones,
 * tablets and some desktop browsers.
 * <p>
 * A new instance of this class is created for every new user and every
 * browser tab/window.
 *
 * @Viewport(VIEWPORT)
 *                     @PWA(
 *                     name = "Inicio de Remuneracion",
 *                     shortName = "Remuneracion",
 *                     startPath = "login",
 *                     backgroundColor = "#227aef",
 *                     themeColor = "#227aef",
 *                     offlinePath = "offline-page.html",
 *                     offlineResources = { "images/offline-login-banner.jpg" })
 */
public class MainView extends AppLayout
{

  private final ConfirmDialog confirmDialog = new ConfirmDialog();

  private final Tabs menu;

  public MainView()
  {

    confirmDialog.setCancelable(true);
    confirmDialog.setConfirmButtonTheme("raised tertiary error");
    confirmDialog.setCancelButtonTheme("raised tertiary");

    this.setDrawerOpened(false);
    Span appName = new Span("Remuneración de Agentes de Transmilenio");
    appName.addClassName("hide-on-mobile");

    menu = createMenuTabs();

    this.addToNavbar(appName);
    this.addToNavbar(true, menu);
    this.getElement().appendChild(confirmDialog.getElement());
    getElement().addEventListener(
        "search-focus", e->
          {
            getElement().getClassList().add("hide-navbar");
          }
    );

    getElement().addEventListener(
        "search-blur", e->
          {
            getElement().getClassList().remove("hide-navbar");
          }
    );
  }// MainView

  @Override
  protected void afterNavigation() {
    super.afterNavigation();
    confirmDialog.setOpened(false);
    if (getContent() instanceof HasConfirmation)
      ((HasConfirmation) getContent()).setConfirmDialog(confirmDialog);

    String              target      = RouteConfiguration.forSessionScope().getUrl(this.getContent().getClass());
    Optional<Component> tabToSelect = menu.getChildren().filter(tab->
                                      {
                                        Component child = tab.getChildren().findFirst().get();
                                        return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
                                      }).findFirst();
    tabToSelect.ifPresent(tab-> menu.setSelectedTab((Tab) tab));

  }// afterNavigation

  private static Tabs createMenuTabs() {
    final Tabs tabs = new Tabs();
    tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
    tabs.add(getAvailableTabs());

    return tabs;

  }// createMenuTabs

  private static Tab[] getAvailableTabs() {
    final List<Tab> tabs = new ArrayList<>(4);
    if (SecurityUtils.isAccessGranted(UsersView.class))
      tabs.add(createTab(VaadinIcon.USER, TITLE_ADMINISTRACION, UsersView.class));

    tabs.add(createTab(VaadinIcon.EDIT, TITLE_DICT, DictView.class));
    tabs.add(createTab(VaadinIcon.ABACUS, TITLE_FORMULACION, FormulacionView.class));
    tabs.add(createTab(VaadinIcon.DOLLAR, TITLE_LIQUIDACION, LiquidadorFrontView.class));
    final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
    final Tab    logoutTab   = createTab(createLogoutLink(contextPath));
    tabs.add(logoutTab);

    return tabs.toArray(new Tab[tabs.size()]);

  }// getAvailableTabs

  private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) { return createTab(populateLink(new RouterLink(null, viewClass), icon, title)); }// createTab

  private static Tab createTab(Component content) {
    final Tab tab = new Tab();
    tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    tab.add(content);
    return tab;
  }// createTab

  private static Anchor createLogoutLink(String contextPath) {
    final Anchor a = populateLink(new Anchor(), VaadinIcon.ARROW_RIGHT, TITLE_LOGOUT);
    // inform vaadin-router that it should ignore this link
    a.setHref(contextPath + "/logout");
    a.getElement().setAttribute("router-ignore", true);
    return a;
  }// createLogoutLink

  private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
    a.add(icon.create());
    a.add(title);
    return a;
  }// populateLink

}// MainView
