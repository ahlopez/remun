package com.transmi.remun.frontend.components.converters;

import static com.transmi.remun.service.util.FormattingUtils.MONTH_AND_DAY_FORMATTER;
import static com.transmi.remun.service.util.FormattingUtils.WEEKDAY_FULLNAME_FORMATTER;

import java.time.LocalDate;

import com.vaadin.flow.templatemodel.ModelEncoder;

/**
 * Date converter specific for the way date is displayed in storefront.
 */
public class LiquidadorLocalDateConverter implements ModelEncoder<LocalDate, LiquidadorDate>
{

  @Override
  public LiquidadorDate encode(LocalDate modelValue) {
    LiquidadorDate result = null;
    if (modelValue != null)
    {
      result = new LiquidadorDate();
      result.setDay(MONTH_AND_DAY_FORMATTER.format(modelValue));
      result.setWeekday(WEEKDAY_FULLNAME_FORMATTER.format(modelValue));
      result.setDate(modelValue.toString());
    }

    return result;
  }// LiquidadorDate

  @Override
  public LocalDate decode(LiquidadorDate storefrontDate) { throw new UnsupportedOperationException(); }

}// LiquidadorLocalDateConverter
