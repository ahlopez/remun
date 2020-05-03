package com.transmi.remun.service.util;

public enum ParmType
{

  NUMBER("DECIMAL"),
  STRING("CADENA"),
  DATE("FECHA");

  String name;

  ParmType(String name)
  { this.name = name; }

  public static ParmType fromValue(String name) {
    for (ParmType p : ParmType.values())
    {
      if (p.name.equals(name))
        return p;
    }
    throw new IllegalArgumentException(name);
  }// fromValue

  /**
   * Return an array with the name of the enum elements
   * 
   * @return String[] Names of the enum elements
   */
  public static String[] getAllTypes() {
    ParmType[] tv    = ParmType.values();
    String[]   types = new String[tv.length];
    for (int i = 0; i < tv.length; i++ )
    {
      types[i] = (tv[i].toString());
    }
    return types;

  }// getAllTypes

  @Override
  public String toString() { return this.name; }

}// ParmType
