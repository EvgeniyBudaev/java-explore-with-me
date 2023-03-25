package ru.practicum.main.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.models.Comment;

import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByEvent_Id(Long eventId, Pageable pageable);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long authorId);
}
