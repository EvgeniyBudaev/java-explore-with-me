package ru.practicum.ewmservice.public_service.service;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.CompilationDto;
import ru.practicum.ewmservice.exceptions.NotFoundValidationException;
import ru.practicum.ewmservice.mapper.CompilationMapper;
import ru.practicum.ewmservice.model.Compilation;
import ru.practicum.ewmservice.model.QCompilation;
import ru.practicum.ewmservice.repository.CompilationRepository;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundValidationException("Compilation with id: " + compId + " nor found"));
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        QCompilation qCompilation = QCompilation.compilation;
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();
        if (pinned != null) {
            expression = expression.and(qCompilation.pinned.eq(pinned));
        }
        List<Compilation> foundCompilations = compilationRepository.findAll(expression, page).getContent();
        return compilationMapper.toCompilationDtoList(foundCompilations);
    }
}
