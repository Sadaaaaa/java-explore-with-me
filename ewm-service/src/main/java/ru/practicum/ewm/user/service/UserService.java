package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(List<Integer> ids, Integer from, Integer size);

    UserDto getUser(int userId);

    UserDto createUser(UserDto userDto);

    void deleteUser(int userId);

    UserDto updateUser(int userId, UserDto userDto);
}
