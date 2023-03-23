package ru.practicum.statservice.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statservice.model.Hit;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {

}
