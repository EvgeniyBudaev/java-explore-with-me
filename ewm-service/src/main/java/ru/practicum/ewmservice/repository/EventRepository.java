package ru.practicum.ewmservice.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.model.Event;
import ru.practicum.ewmservice.model.EventState;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findEventsByInitiatorIdOrderById(Long userId, Pageable page);

    Optional<Event> findEventByInitiatorIdAndId(Long userId, Long eventId);

    Optional<Event> findEventByIdAndState(Long eventId, EventState published);

    Integer countEventByCategoryId(long catId);

    List<Event> findEventsByIdIn(List<Long> eventIds);

    @Modifying
    @Query(value = "" +
            "UPDATE Event e " +
            "SET e.confirmedRequests = :requests " +
            "WHERE e.id = :id")
    void setEventConfirmedRequests(@Param ("id") Long eventId, @Param("requests") Long confirmedRequests);
}
