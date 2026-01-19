package com.daksh.expense_splitter.dto.group;

import jdk.dynalink.linker.LinkerServices;
import lombok.Data;

import java.util.List;
@Data

public class UpdateGroupDto {
    private String name;
    private List<String> memberName;
}
