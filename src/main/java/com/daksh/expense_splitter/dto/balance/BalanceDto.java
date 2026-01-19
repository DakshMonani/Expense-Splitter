package com.daksh.expense_splitter.dto.balance;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceDto {
    private String user;
    private Double amountToReceive;
    private Double amountToPay;
}
