package com.transmi.remun.frontend;

import static com.transmi.remun.service.util.FrontConst.VIEWPORT;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;

@Viewport(VIEWPORT)
@PWA(name = "Inicio de Remuneracion", shortName = "Remuneracion", startPath = "login", backgroundColor = "#227aef", themeColor = "#227aef", offlinePath = "offline-page.html", offlineResources = { "images/offline-login-banner.jpg" })
public class AppShell implements AppShellConfigurator
{
}
