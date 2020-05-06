package com.transmi.remun.frontend.components.converters;

import com.vaadin.flow.templatemodel.ModelEncoder;

public class BoolConverter implements ModelEncoder<Boolean, String>
{

  @Override
  public String encode(Boolean modelValue) { return modelValue ?
      "SI" :
      "NO"; }

  @Override
  public Boolean decode(String presentationValue) { return Boolean.parseBoolean(presentationValue); }

}// BoolConverter
