package com.daksh.expense_splitter.service;

import com.daksh.expense_splitter.dto.settlment.SettlementDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SettlementService {
    List<SettlementDto> calculateSettlements(String groupId);
}
