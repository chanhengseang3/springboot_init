package com.loan24.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Converter
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute == null ? null : attribute.toSeconds();
    }

    @Override
    public Duration convertToEntityAttribute(Long duration) {
        return duration == null ? null : Duration.of(duration, ChronoUnit.SECONDS);
    }
}