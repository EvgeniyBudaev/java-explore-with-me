package ru.practicum.statservice.service;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.statdto.dto.HitDto;
import ru.practicum.statdto.dto.StatDto;
import ru.practicum.statservice.model.Hit;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatService {

    HitDto addHits(Hit hit);

    List<StatDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime start,
                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime end,
                           @RequestParam List<String> uris,
                           @RequestParam(defaultValue = "false") boolean unique);
}