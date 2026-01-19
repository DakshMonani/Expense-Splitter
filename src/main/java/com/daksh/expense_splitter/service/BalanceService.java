package com.daksh.expense_splitter.service;

import com.daksh.expense_splitter.dto.balance.BalanceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BalanceService {
    List<BalanceDto> calculateBalances(String groupId);
}
