package com.transmi.remun.frontend.components.converters;

import com.transmi.remun.service.util.HasLogger;
import com.transmi.remun.service.util.TransmiPhase;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class PhaseConverter implements ModelEncoder<TransmiPhase, String>, HasLogger
{

  public PhaseConverter()
  {}

  /*
   * private Map<String, TransmiPhase> values;
   * public PhaseConverter()
   * { values = Arrays.stream(TransmiPhase.values())
   * .collect(Collectors.toMap(TransmiPhase::toString, Function.identity())); }
   */
  @Override
  public TransmiPhase decode(String presentationValue) {
    // return convertIfNotNull(presentationValue, TransmiPhase::fromValue);
    getLogger().info(">>> Llegué a phase decode:" + presentationValue);
    return presentationValue == null ?
        null :
        TransmiPhase.valueOf(presentationValue);

  }

  @Override
  public String encode(TransmiPhase modelValue) {
    getLogger().info(">>> Llegué a phase encode:" + modelValue.name() + "->" + modelValue.toString());
    return modelValue == null ?
        null :
        modelValue.toString();
    // return convertIfNotNull(modelValue, TransmiPhase::toString);
  }

}// PhaseConverter
