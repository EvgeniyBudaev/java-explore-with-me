package ru.practicum.statservice.mapper;
import org.mapstruct.Mapper;
import ru.practicum.statdto.dto.HitDto;
import ru.practicum.statservice.model.Hit;

@Mapper(componentModel = "spring")
public interface StatMapper {

    HitDto toHitDto(Hit hit);
}
