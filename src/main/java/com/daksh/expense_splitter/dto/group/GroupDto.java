package com.daksh.expense_splitter.dto.group;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {
    @Id
    private String id;
    @NotBlank(message = "Group name is required")
    private String name;

    /**
     * Require at least one member when creating a group.
     * Use container element constraint to ensure each id string is not blank.
     * (Requires Bean Validation 2.0 / Java 8+)
     */
    @NotEmpty(message = "member list must contain at least one member ")
    private List<String> memberName;

//    public GroupDto(String id, String name, List<String> memberIds) {
//        this.id = id;
//        this.name = name;
//        this.memberIds = memberIds != null ? memberIds : new ArrayList<>();
//    }
}