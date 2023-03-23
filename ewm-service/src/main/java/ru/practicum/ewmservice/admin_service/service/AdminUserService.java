package ru.practicum.ewmservice.admin_service.service;

import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.dto.NewUserRequest;
import ru.practicum.ewmservice.dto.UserDto;

import java.util.List;

@Service
public interface AdminUserService {

    UserDto addUser(NewUserRequest newUserRequest);

    void removeUser(Long userId);

    List<UserDto> getAllUsers(List<Long> ids, int from, int size);
}
