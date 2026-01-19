package com.daksh.expense_splitter.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
@Data

public class CreateGroupDto {
    @NotBlank(message = "Group name is required")
    private String name;

    /**
     * Require at least one member when creating a group.
     * Use container element constraint to ensure each id string is not blank.
     * (Requires Bean Validation 2.0 / Java 8+)
     */
    @NotEmpty(message = "member list must contain at least one member name")
    private List<@NotBlank(message = "member cannot be blank") String> memberName;
}
