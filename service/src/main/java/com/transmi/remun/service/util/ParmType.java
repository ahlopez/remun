package com.transmi.remun.service.util;

import java.util.ArrayList;
import java.util.List;

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

  public static String[] getAllTypes() {
    List<String> types = new ArrayList<>();
    for (ParmType p : ParmType.values())
    {
      types.add(p.name);
    }

    return (String[]) types.toArray();
  }// getAllTypes

  @Override
  public String toString() { return this.name; }

}// ParmType
