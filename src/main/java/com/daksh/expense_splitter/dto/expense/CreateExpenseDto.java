package com.daksh.expense_splitter.dto.expense;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Data
public class CreateExpenseDto {
    private String groupId;            // reference to group document
    private String paidBy; // userId of payer
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount should be greater than 0")
    private Double amount;             // total expense

    private List<String> splitAmong;   // userIds who share the cost

    private String description;        // optional title (ex: Dinner)
    private String category;           // Food, Travel, Shopping, Other...

    private Instant timestamp;         // when expense occurred
}
