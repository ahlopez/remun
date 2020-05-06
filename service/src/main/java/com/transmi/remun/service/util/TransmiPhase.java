package com.transmi.remun.service.util;

public enum TransmiPhase implements HasLogger
{

  F1,
  F2,
  F3,
  F4,
  TODAS;

  public String getName() {
    getLogger().info(">>> Transmiphase.getName()=" + name());
    return name();
  }

  public static TransmiPhase[] getAllPhases() { return values(); }

  @Override
  public String toString() { return name(); }// toString

}// TransmiPhase
