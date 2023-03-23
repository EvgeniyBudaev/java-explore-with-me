package ru.practicum.statdto.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatDto {

    private String app;
    private String uri;
    private Long hits;
}