package com.mleczey.warehouse.configuration;

import java.time.LocalDate;
import javax.ws.rs.ext.ParamConverter;

public class LocalDateParamConverter implements ParamConverter<LocalDate> {

  @Override
  public LocalDate fromString(String s) {
    LocalDate result = null;
    if (null != s) {
      result = LocalDate.parse(s);
    }
    return result;
  }

  @Override
  public String toString(LocalDate localDate) {
    String result = null;
    if (null != localDate) {
      result = localDate.toString();
    }
    return result;
  }

}
