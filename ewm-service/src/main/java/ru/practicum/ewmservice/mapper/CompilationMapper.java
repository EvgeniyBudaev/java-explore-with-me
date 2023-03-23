package ru.practicum.ewmservice.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewmservice.dto.CompilationDto;
import ru.practicum.ewmservice.dto.NewCompilationDto;
import ru.practicum.ewmservice.model.Compilation;
import ru.practicum.ewmservice.model.Event;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    @Mapping(target = "events", source = "eventSet")
    Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> eventSet);

    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> toCompilationDtoList(List<Compilation> compilationList);
}
