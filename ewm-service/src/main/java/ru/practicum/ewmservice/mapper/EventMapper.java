package ru.practicum.ewmservice.mapper;
import org.mapstruct.*;
import ru.practicum.ewmservice.dto.*;
import ru.practicum.ewmservice.model.Event;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring", uses = {CategoryMapper.class, UserMapper.class})
public interface EventMapper {

    EventFullDto toEventFullDto(Event event);

    List<EventFullDto> toEventFullDtoList(List<Event> eventList);

    @Mapping(source = "category", target = "category.id")
    Event toEvent(NewEventDto newEventDto);

    EventShortDto toEventShortDto(Event event);

    List<EventShortDto> toEventShortDtoList(List<Event> eventList);

    @Mapping(source = "category.id", target = "category")
    UpdateEventUserRequest toUpdateEventUserRequest(Event event);
}
