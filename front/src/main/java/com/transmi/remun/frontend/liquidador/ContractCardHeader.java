package com.transmi.remun.frontend.liquidador;

public class ContractCardHeader
{

  private String main;

  private String secondary;

  public ContractCardHeader(String main, String secondary)
  {
    this.main      = main;
    this.secondary = secondary;
  }// ContractCardHeader

  public String getMain() { return main; }

  public void setMain(String main) { this.main = main; }

  public String getSecondary() { return secondary; }

  public void setSecondary(String secondary) { this.secondary = secondary; }

}// ContractCardHeader
