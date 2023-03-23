package ru.practicum.statdto.dto;
import lombok.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class HitShortDto {
    @NotBlank
   private String app;
    @NotBlank
   private String uri;
    @NotBlank
   private String ip;
}
