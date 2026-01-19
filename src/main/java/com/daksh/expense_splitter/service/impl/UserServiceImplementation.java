package com.daksh.expense_splitter.service.impl;

import com.daksh.expense_splitter.dto.user.CreateUserDto;
import com.daksh.expense_splitter.dto.user.Setpassword;
import com.daksh.expense_splitter.dto.user.UpdateUserDto;
import com.daksh.expense_splitter.dto.user.UserDto;
import com.daksh.expense_splitter.model.User;
import com.daksh.expense_splitter.repository.UserRepository;
import com.daksh.expense_splitter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> findAll() {
       return userRepository.findAll().stream().map(user -> modelMapper.map(user,UserDto.class)).toList();
    }

    @Override
    public UserDto findById(String id) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with that id not found"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto createNewUser(CreateUserDto createUserDto) {

        User user = modelMapper.map(createUserDto, User.class);

        // 🔐 encode password explicitly
        user.setPassword(
                passwordEncoder.encode(createUserDto.getPassword())
        );

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public void deleteUserById(String id) {

        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("user with that id not found");
        }
    }

    @Override
    public UserDto updatePartialUser(String id, Map<String, Object> updates) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with that id not found"));


        updates.forEach((fieldName,fieldValue)->{
            if (fieldName.equalsIgnoreCase("id") ||fieldName.equalsIgnoreCase("password") ) {
                return;  // ignore if client sends "id" in PATCH body
            }
            Field field = ReflectionUtils.findField(User.class,fieldName);
            if (field != null){
                field.setAccessible(true);
                ReflectionUtils.setField(field,user,fieldValue);
            }else {
                throw new IllegalArgumentException("Field '" + fieldName + "' does not exist");
            }

        });
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser,UserDto.class);

    }

    @Override
    public UserDto updateUser(String id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with that id not found"));

        user.setName(updateUserDto.getName());
        user.setEmail(updateUserDto.getEmail());
        user.setPhone(updateUserDto.getPhone());
         User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public void setPasswordById(String id, Setpassword setpassword) {
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("user with that id not found"));
        user.setPassword(passwordEncoder.encode(setpassword.getPassword()));
        User updatedUser = userRepository.save(user);
    }
}
