package com.daksh.expense_splitter.controller.balance;

import com.daksh.expense_splitter.dto.balance.BalanceDto;
import com.daksh.expense_splitter.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/groups/{groupId}/balances")
public class BalanceContoroller {
    private final BalanceService balanceService;
    @GetMapping
    public ResponseEntity<List<BalanceDto>> getGroupBalance(@PathVariable String groupId){
        return ResponseEntity.ok(balanceService.calculateBalances(groupId));
    }
}
