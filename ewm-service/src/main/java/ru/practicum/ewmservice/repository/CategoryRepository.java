package ru.practicum.ewmservice.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.model.Category;
import java.util.List;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @Query(value = "SELECT c.name FROM Category c")
    List<String> findCategoryByName();
}
