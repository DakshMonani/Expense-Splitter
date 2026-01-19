package com.daksh.expense_splitter.service;

import com.daksh.expense_splitter.dto.user.CreateUserDto;
import com.daksh.expense_splitter.dto.user.Setpassword;
import com.daksh.expense_splitter.dto.user.UpdateUserDto;
import com.daksh.expense_splitter.dto.user.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {


    public List<UserDto> findAll();

    UserDto findById(String id);

    UserDto createNewUser(CreateUserDto createUserDto);

    void deleteUserById(String id);

    UserDto updatePartialUser(String id, Map<String, Object> updates);

    UserDto updateUser(String id, UpdateUserDto updateUserDto);

    void setPasswordById(String id, Setpassword setpassword);
}
