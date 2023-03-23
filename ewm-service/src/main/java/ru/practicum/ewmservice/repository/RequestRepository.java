package ru.practicum.ewmservice.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.model.ParticipationRequest;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends PagingAndSortingRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findRequestByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findRequestByRequesterId(Long userId);

    List<ParticipationRequest> findByEventId(long eventId);

    @Query(value = "" +
            "SELECT r " +
            "FROM ParticipationRequest r " +
            "WHERE r.event.id = :eventId AND " +
            "r.id IN :requestIds")
    List<ParticipationRequest> findRequestsForUpdate(@Param("eventId") Long eventId, @Param("requestIds") List<Long> requestIds);
}
