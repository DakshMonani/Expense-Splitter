package com.daksh.expense_splitter.model;

import com.daksh.expense_splitter.helper.MemberInfo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor  // Add this - Builder needs it
@Builder
@Document(collection = "groups")
public class Group {
    @Id
    private String id;
    private String name;

//    @Builder.Default  // Add this for builder to use the default value
    private List<MemberInfo> members = new ArrayList<>();
}