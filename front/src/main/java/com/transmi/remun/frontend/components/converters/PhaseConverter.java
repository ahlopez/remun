package com.transmi.remun.frontend.components.converters;

import static com.transmi.remun.frontend.components.DataProviderUtil.convertIfNotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.transmi.remun.service.util.TransmiPhase;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class PhaseConverter implements ModelEncoder<TransmiPhase, String>
{

  private Map<String, TransmiPhase> values;

  public PhaseConverter()
  { values = Arrays.stream(TransmiPhase.values())
      .collect(Collectors.toMap(TransmiPhase::toString, Function.identity())); }

  @Override
  public TransmiPhase decode(String presentationValue) { return convertIfNotNull(presentationValue, values::get); }

  @Override
  public String encode(TransmiPhase modelValue) { return convertIfNotNull(modelValue, TransmiPhase::toString); }

}// PhaseConverter
