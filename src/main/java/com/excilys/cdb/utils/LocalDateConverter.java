package main.java.com.excilys.cdb.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LocalDateConverter implements AttributeConverter<java.time.LocalDate, java.sql.Timestamp> {

  @Override
  public java.sql.Timestamp convertToDatabaseColumn(java.time.LocalDate entityValue) {
    return entityValue == null ? null : java.sql.Timestamp.valueOf(entityValue.atStartOfDay());
  }

  @Override
  public java.time.LocalDate convertToEntityAttribute(java.sql.Timestamp dbValue) {
    return dbValue == null ? null : dbValue.toLocalDateTime().toLocalDate(); 
  }
}