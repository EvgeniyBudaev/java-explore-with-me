package ru.practicum.ewmservice.admin_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.exceptions.ConflictException;
import ru.practicum.ewmservice.repository.UserRepository;
import ru.practicum.ewmservice.dto.NewUserRequest;
import ru.practicum.ewmservice.dto.UserDto;
import ru.practicum.ewmservice.mapper.UserMapper;
import ru.practicum.ewmservice.model.User;
import ru.practicum.ewmservice.exceptions.NotFoundValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        UserDto userDto = userMapper.toUserDto(newUserRequest);
        validate(userDto);
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    public void removeUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundValidationException("User with id: " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        PageRequest usersSortedByIdAsc = PageRequest.of(from, size);
        List<User> users;
        if (ids != null) {
            users = userRepository.findAllByIdInOrderById(ids, usersSortedByIdAsc).toList();
        } else {
            users = userRepository.findAll(usersSortedByIdAsc).toList();
        }
        return userMapper.toUserDtoList(users);
    }

    private void validate(UserDto userDto) {
        Set<String> userNames = new HashSet<>(userRepository.findUserNames());
        if (userNames.contains(userDto.getName())) {
            throw new ConflictException("User name: " + userDto.getName() + " already used");
        }
    }
}
