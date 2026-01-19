package com.daksh.expense_splitter.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "expenses")
public class Expense {

    @Id
    private String id;

    private String groupId;            // reference to group document
    private String paidBy;             // userId of payer
    private Double amount;             // total expense

    private List<String> splitAmong = new ArrayList<>();   // userIds who share the cost

    private String description;        // optional title (ex: Dinner)
    private String category;           // Food, Travel, Shopping, Other...

    private Instant timestamp;         // when expense occurred

    private Instant createdAt;         // audit
    private Instant updatedAt;         // audit
}
