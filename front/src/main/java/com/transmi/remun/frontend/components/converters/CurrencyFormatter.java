package com.transmi.remun.frontend.components.converters;

import com.transmi.remun.frontend.components.DataProviderUtil;
import com.transmi.remun.service.util.FormattingUtils;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class CurrencyFormatter implements ModelEncoder<Integer, String>
{

   @Override
   public String encode(Integer modelValue) {
      return DataProviderUtil.convertIfNotNull(modelValue, FormattingUtils::formatAsCurrency);
   }

   @Override
   public Integer decode(String presentationValue) {
      throw new UnsupportedOperationException();
   }
}//CurrencyFormatter
