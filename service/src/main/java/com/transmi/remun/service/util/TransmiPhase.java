package com.transmi.remun.service.util;

public enum TransmiPhase
{

  I("F1"),
  II("F2"),
  III("F3"),
  IV("F4"),
  G("TODAS");

  String name;

  TransmiPhase(String name)
  { this.name = name; }

  public String value() { return name; }

  public static TransmiPhase fromValue(String name) {
    for (TransmiPhase p : TransmiPhase.values())
    {
      if (p.name.equalsIgnoreCase(name))
        return p;
    }
    throw new IllegalArgumentException(name);
  }// fromValue

  /**
   * Return an array with the name of the enum elements
   * 
   * @return String[] Names of the enum elements
   */
  public static TransmiPhase[] getAllPhases() { return values(); }
  /*
   * TransmiPhase[] fv = TransmiPhase.values();
   * String[] phases = new String[fv.length];
   * for (int i = 0; i < fv.length; i++ )
   * {
   * phases[i] = (fv[i].toString());
   * }
   * return phases;
   * }// getAllPhases
   */

  /**
   * Aplana la estructura del enum en un String
   *
   * @return String String con la representacion del enum
   */
  @Override
  public String toString() { return name; }// toString

}// TransmiPhase
