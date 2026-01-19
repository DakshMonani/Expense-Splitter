package com.daksh.expense_splitter.dto.expense;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class UpdateExpenseDto {
    private String groupId;            // reference to group document
    private String paidBy;             // userId of payer
    private Double amount;             // total expense

    private List<String> splitAmong;   // userIds who share the cost

    private String description;        // optional title (ex: Dinner)
    private String category;           // Food, Travel, Shopping, Other...

    private Instant timestamp;         // when expense occurred

    private Instant createdAt;         // audit
    private Instant updatedAt;          //audit
}
