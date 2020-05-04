package com.transmi.remun.frontend.components.converters;

import com.transmi.remun.service.util.ContractStatus;
import com.transmi.remun.service.util.HasLogger;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class ContractStateConverter implements ModelEncoder<ContractStatus, String>, HasLogger
{

  public ContractStateConverter()
  {}

  /*
   * private Map<String, ContractStatus> values;
   * public ContractStateConverter()
   * { values = Arrays.stream(ContractStatus.values())
   * .collect(Collectors.toMap(ContractStatus::toString, Function.identity())); }
   */

  @Override
  public ContractStatus decode(String presentationValue) {
    // return convertIfNotNull(presentationValue, values::get);
    getLogger().info(">>> Llegué a status decode:" + presentationValue);
    return presentationValue == null ?
        null :
        ContractStatus.valueOf(presentationValue);
  }

  @Override
  public String encode(ContractStatus modelValue) {
    // return convertIfNotNull(modelValue, ContractStatus::toString);
    getLogger().info(">>> Llegué a status encode:" + modelValue.name() + "->" + modelValue.toString());
    return modelValue == null ?
        null :
        modelValue.toString();

  }

}// ContractStateConverter
