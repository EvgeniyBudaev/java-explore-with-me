package ru.practicum.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main.enums.StateActionForAdmin;
import ru.practicum.main.models.Location;
import ru.practicum.main.constants.Pattern;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEventAdminDto {
    @Size(min = 3, max = 500)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 2000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Pattern.DATE)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private StateActionForAdmin stateAction;
    @Size(min = 2, max = 120)
    private String title;
}

