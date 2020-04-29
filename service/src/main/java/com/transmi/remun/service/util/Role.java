package com.transmi.remun.service.util;

/*
public enum Role
{

  LIQUIDA("liquidador"),
  VISUALIZA("Visualizador"),
  ADMIN("Admin");  // This role implicitly allows access to all views.

  public static final String LIQUIDADOR = LIQUIDA.getName();

  public static final String VISUALIZADOR = VISUALIZA.getName();

  public static final String ADMINISTRA = ADMIN.getName();

  private String name;

  public String getName() { return name; }

  private Role(String name)
  { this.name = name; }

  public static String[] getAllRoles() {
    List<String> roles = new ArrayList<>();
    for (Role r : Role.values())
    { roles.add(r.name); }

    return (String[]) roles.toArray();
  }// getAllRoles

}// Role
*/

public class Role
{
  public static final String LIQUIDADOR = "liquidador";

  public static final String REVISOR = "revisor";

  // This role implicitly allows access to all views.
  public static final String ADMIN = "admin";

  private Role()
  {
    // Static methods and fields only
  }

  public static String[] getAllRoles() { return new String[] { LIQUIDADOR, REVISOR, ADMIN }; }

}// Role
