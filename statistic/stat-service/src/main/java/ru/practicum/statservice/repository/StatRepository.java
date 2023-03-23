package ru.practicum.statservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.statdto.dto.StatDto;
import ru.practicum.statservice.model.Hit;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.statdto.dto.StatDto(e.app, e.uri, count(distinct e.ip)) " +
            "from Hit e " +
            "where e.timestamp between ?2 and ?3 " +
            "and e.uri in ?1 " +
            "group by e.app, e.uri")
    List<StatDto> findStatWithUnique(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.statdto.dto.StatDto(e.app, e.uri, count(e.ip)) " +
            "from Hit e " +
            "where e.timestamp between ?2 and ?3 " +
            "and e.uri in ?1 " +
            "group by e.app, e.uri")
    List<StatDto> findStatNOtUnique(List<String> uris, LocalDateTime start, LocalDateTime end);
}