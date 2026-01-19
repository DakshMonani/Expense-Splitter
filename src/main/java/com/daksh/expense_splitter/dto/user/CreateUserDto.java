package com.daksh.expense_splitter.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data

public class CreateUserDto {
    @NotBlank(message="Name required")
    private String name;

    @Email
    @NotBlank
    private String email;

    @Size(min=10, max=10)
    private String phone;


    private String password;
}
