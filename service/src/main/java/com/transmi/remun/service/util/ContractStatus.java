package com.transmi.remun.service.util;

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

}// ContractState
