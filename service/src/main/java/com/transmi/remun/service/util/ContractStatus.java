package com.transmi.remun.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public enum ContractStatus
{

  VIGENTE, CERRADO;

  /**
   * Gets a version of the enum identifier in a human friendly format.
   *
   * @return a human friendly version of the identifier
   */
  public String getDisplayName() { return Utility.capitalize(name().toUpperCase(Locale.ENGLISH)); }

  public static String[] getAllStatus() {
    List<String> status = new ArrayList<>();
    for (ContractStatus s : ContractStatus.values())
    {
      status.add(s.toString());
    }

    return (String[]) status.toArray();
  }// getAllStatus

}// ContractState
