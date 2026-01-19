package com.daksh.expense_splitter.dto.expense;

import com.daksh.expense_splitter.model.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseDto {
    @Id
    private String id;

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
