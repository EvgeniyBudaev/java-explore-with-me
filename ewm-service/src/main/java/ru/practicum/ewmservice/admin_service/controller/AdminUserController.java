package ru.practicum.ewmservice.admin_service.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin_service.service.AdminUserService;
import ru.practicum.ewmservice.dto.NewUserRequest;
import ru.practicum.ewmservice.dto.UserDto;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequestMapping("/admin/users")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping()
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("POST: /admin/users");
        return ResponseEntity.status(201).body(adminUserService.addUser(newUserRequest));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminUserService.removeUser(userId);
        log.info("DELETE: /admin/users/{}", userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>>  getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("GET: /admin/users?ids={}&from={}&size={}", ids, from, size);
        return ResponseEntity.ok().body(adminUserService.getAllUsers(ids, from, size));
    }
}
