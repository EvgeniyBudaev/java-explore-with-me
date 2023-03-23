package ru.practicum.ewmservice.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.model.User;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Page<User> findAllByIdInOrderById(List<Long> ids, Pageable pageable);

    @Query(value = "SELECT u.name FROM User u")
    List<String> findUserNames();
}
