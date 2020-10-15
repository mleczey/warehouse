package com.mleczey.warehouse.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class LocalDateParamConverterProvider implements ParamConverterProvider {

  @Override
  @SuppressWarnings("unchecked")
  public <T> ParamConverter<T> getConverter(Class<T> rawType, Type type, Annotation[] annotations) {
    ParamConverter<T> paramConverter = null;
    if (rawType.equals(LocalDate.class)) {
      paramConverter = (ParamConverter<T>) new LocalDateParamConverter();
    }
    return paramConverter;
  }

}
