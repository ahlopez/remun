package com.transmi.remun.service.util;

import java.util.Locale;

import org.springframework.data.domain.Sort;

public class FrontConst
{

  public static final Locale APP_LOCALE = Locale.US;

  public static final String PAGE_ROOT = "";

  public static final String PAGE_LIQUIDADOR_FRONT = "liquidadorfront";

  public static final String PAGE_LIQUIDADOR_FRONT_EDIT = "liquidadorfront/edit";

  public static final String PAGE_DASHBOARD = "dashboard";

  public static final String PAGE_USERS = "users";

  public static final String PAGE_DICT = "dict";

  public static final String PAGE_FORMULACION = "formulacion";

  public static final String PAGE_PRODUCTS = "products";

  public static final String TITLE_LIQUIDADOR_FRONT = "Liquidadorfront";

  public static final String TITLE_DASHBOARD = "Dashboard";

  public static final String TITLE_USERS = "Users";

  public static final String TITLE_PRODUCTS = "Products";

  public static final String TITLE_DICT = "Diccionario";

  public static final String TITLE_FORMULACION = "Formulación";

  public static final String TITLE_LIQUIDACION = "Liquidación";

  public static final String TITLE_ADMINISTRACION = "Administración";

  public static final String TITLE_LOGOUT = "Salida";

  public static final String TITLE_NOT_FOUND = "Página no encontrada";

  public static final String TITLE_ACCESS_DENIED = "Acceso no permitido";

  public static final String[] ORDER_SORT_FIELDS = { "toDate", "fromDate", "id" };

  public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

  public static final String VIEWPORT = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover";

  // Mutable for testing.
  public static int NOTIFICATION_DURATION = 8000;

}// RemunConst
