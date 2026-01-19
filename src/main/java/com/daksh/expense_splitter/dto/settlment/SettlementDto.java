package com.daksh.expense_splitter.dto.settlment;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class SettlementDto {
    private String from;
    private String to;
    private double amount;
}
