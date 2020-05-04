package com.transmi.remun.frontend.components;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.transmi.remun.service.util.HasLogger;
import com.vaadin.flow.component.ItemLabelGenerator;

public class DataProviderUtil implements HasLogger
{

  public static <S, T> T convertIfNotNull(S source, Function<S, T> converter) { return convertIfNotNull(source, converter, ()-> null); }

  public static <S, T> T convertIfNotNull(S source, Function<S, T> converter, Supplier<T> nullValueSupplier) { return source != null ?
      converter.apply(source) :
      nullValueSupplier.get(); }

  public static <T> ItemLabelGenerator<T> createItemLabelGenerator(Function<T, String> converter) {
    Logger.getGlobal().info(">>> en createItemLabel:" + converter.getClass().getName());
    return item-> convertIfNotNull(item, converter, ()-> "");
  }

}// DataProviderUtil
