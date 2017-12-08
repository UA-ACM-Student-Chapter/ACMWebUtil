package edu.ua.cs.acm.converters;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by jzarobsky on 11/21/17.
 */
// Shamelessly stolen from https://dzone.com/articles/storing-java-8-localdatetime-as-timestamp-postgres
public class LocalDateTimeConverter implements AttributeConverter <LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return attribute != null ? Timestamp.valueOf(attribute) : null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbData) {
        return dbData != null ? dbData.toLocalDateTime() : null;
    }
}
