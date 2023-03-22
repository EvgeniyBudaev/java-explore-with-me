package ru.practicum.statsservice.mapper;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringDateConverter implements Converter<String, LocalDateTime> {


    @Override
    public LocalDateTime convert(@NonNull String source) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(source, format);
    }
}