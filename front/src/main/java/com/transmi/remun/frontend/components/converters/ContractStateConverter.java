package com.transmi.remun.frontend.components.converters;

import static com.transmi.remun.frontend.components.DataProviderUtil.convertIfNotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.transmi.remun.service.util.ContractStatus;
import com.vaadin.flow.templatemodel.ModelEncoder;

public class ContractStateConverter implements ModelEncoder<ContractStatus, String>
{

  private Map<String, ContractStatus> values;

  public ContractStateConverter()
  { values = Arrays.stream(ContractStatus.values())
      .collect(Collectors.toMap(ContractStatus::toString, Function.identity())); }

  @Override
  public ContractStatus decode(String presentationValue) { return convertIfNotNull(presentationValue, values::get); }

  @Override
  public String encode(ContractStatus modelValue) { return convertIfNotNull(modelValue, ContractStatus::toString); }

}// ContractStateConverter
