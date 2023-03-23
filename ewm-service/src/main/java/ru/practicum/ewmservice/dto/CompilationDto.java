package ru.practicum.ewmservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private Long id;

    private Set<EventFullDto> events;

    private Boolean pinned;

    private String title;
}
