package com.daksh.expense_splitter.service.impl;

import com.daksh.expense_splitter.dto.balance.BalanceDto;
import com.daksh.expense_splitter.dto.settlment.SettlementDto;
import com.daksh.expense_splitter.model.Group;
import com.daksh.expense_splitter.repository.GroupRepositry;
import com.daksh.expense_splitter.service.BalanceService;
import com.daksh.expense_splitter.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class SettlementServiceImplementation implements SettlementService {

    private final BalanceService balanceService;
    private final GroupRepositry groupRepositry;

    @Override
    public List<SettlementDto> calculateSettlements(String groupId) {

        // 1️⃣ Validate group exists
        groupRepositry.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // 2️⃣ Get balances (name-based)
        List<BalanceDto> balances = balanceService.calculateBalances(groupId);

        // 3️⃣ Separate debtors and creditors
        List<BalanceDto> debtors = new ArrayList<>();
        List<BalanceDto> creditors = new ArrayList<>();

        for (BalanceDto balance : balances) {
            if (balance.getAmountToPay() > 0) {
                debtors.add(balance);
            } else if (balance.getAmountToReceive() > 0) {
                creditors.add(balance);
            }
        }

        // 4️⃣ Settlement calculation
        List<SettlementDto> settlements = new ArrayList<>();

        int i = 0; // debtor index
        int j = 0; // creditor index

        while (i < debtors.size() && j < creditors.size()) {

            BalanceDto debtor = debtors.get(i);
            BalanceDto creditor = creditors.get(j);

            double settleAmount = Math.min(
                    debtor.getAmountToPay(),
                    creditor.getAmountToReceive()
            );

            // create settlement
            settlements.add(
                    SettlementDto.builder()
                            .from(debtor.getUser())
                            .to(creditor.getUser())
                            .amount(settleAmount)
                            .build()
            );

            // update remaining amounts
            debtor.setAmountToPay(debtor.getAmountToPay() - settleAmount);
            creditor.setAmountToReceive(creditor.getAmountToReceive() - settleAmount);

            // move pointers
            if (debtor.getAmountToPay() == 0) {
                i++;
            }
            if (creditor.getAmountToReceive() == 0) {
                j++;
            }
        }

        return settlements;
    }
}
