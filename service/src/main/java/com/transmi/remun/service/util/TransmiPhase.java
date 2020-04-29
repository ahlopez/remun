package com.transmi.remun.service.util;

import java.util.ArrayList;
import java.util.List;

public enum TransmiPhase
{

  I("Fase 1"),
  II("Fase 2"),
  III("Fase 3"),
  IV("Fase 4"),
  G("TODAS");

  String name;

  TransmiPhase(String name)
  { this.name = name; }

  public String value() { return name; }

  public static TransmiPhase fromValue(String name) {
    for (TransmiPhase p : TransmiPhase.values())
    {
      if (p.name.equals(name))
        return p;
    }
    throw new IllegalArgumentException(name);
  }// fromValue

  public static String[] getAllPhases() {
    List<String> phases = new ArrayList<>();
    for (TransmiPhase p : TransmiPhase.values())
    {
      phases.add(p.name);
    }

    return (String[]) phases.toArray();
  }// getAllPhases

  /**
   * Aplana la estructura del enum en un String
   *
   * @return String String con la representacion del enum
   */
  @Override
  public String toString() { return name; }// toString

}// TransmiPhase
