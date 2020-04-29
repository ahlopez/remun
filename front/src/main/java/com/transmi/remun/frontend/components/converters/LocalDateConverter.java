package com.transmi.remun.frontend.components.converters;

import static com.transmi.remun.frontend.components.DataProviderUtil.convertIfNotNull;
import static com.transmi.remun.service.util.FormattingUtils.FULL_DATE_FORMATTER;

import java.time.LocalDate;

import com.vaadin.flow.templatemodel.ModelEncoder;

public class LocalDateConverter implements ModelEncoder<LocalDate, String>
{

  @Override
  public String encode(LocalDate modelValue) { return convertIfNotNull(modelValue, v-> FULL_DATE_FORMATTER.format(v)); }

  @Override
  public LocalDate decode(String presentationValue) { throw new UnsupportedOperationException(); }

}// LocalDateConverter
