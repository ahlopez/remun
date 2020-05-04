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

  /**
   * Return an array with the name of the enum elements
   * 
   * @return String[] Names of the enum elements
   */
  public static ContractStatus[] getAllStatus() { return values(); }
  /*
   * ContractStatus[] cv = ContractStatus.values();
   * String[] status = new String[cv.length];
   * for (int i = 0; i < cv.length; i++ )
   * {
   * status[i] = (cv[i].toString());
   * }
   * return status;
   * }// getAllStatus
   */

}// ContractState
