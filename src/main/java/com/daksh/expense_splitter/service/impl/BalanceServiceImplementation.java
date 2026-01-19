package com.daksh.expense_splitter.service.impl;

import com.daksh.expense_splitter.dto.balance.BalanceDto;
import com.daksh.expense_splitter.helper.MemberInfo;
import com.daksh.expense_splitter.model.Expense;
import com.daksh.expense_splitter.model.Group;
import com.daksh.expense_splitter.repository.ExpenseRepositry;
import com.daksh.expense_splitter.repository.GroupRepositry;
import com.daksh.expense_splitter.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceServiceImplementation implements BalanceService {
    private final GroupRepositry groupRepositry;
    private final ExpenseRepositry expenseRepositry;

    @Override
    public List<BalanceDto> calculateBalances(String groupId) {
        Group group = groupRepositry.findById(groupId).orElseThrow(()->new IllegalArgumentException("group with that id not found"));
        List<Expense> expenses = expenseRepositry.findByGroupId(groupId);

        Map<String,Double> balanceMap = new HashMap<>();
        for (MemberInfo member : group.getMembers()){
            balanceMap.put(member.getId(), 0.0);
        }

        for (Expense expense:expenses){
            if (expense.getSplitAmong().isEmpty()) {
                throw new IllegalStateException("Expense has no split members");
            }
            double share = Math.round((expense.getAmount()/expense.getSplitAmong().size())*100)/100;
            for (String userId : expense.getSplitAmong()){
                balanceMap.put(userId,balanceMap.get(userId)-share);
            }
            String paidby = expense.getPaidBy();
            balanceMap.put(paidby, balanceMap.get(paidby)+expense.getAmount());
        }
        Map<String,String> userIdToName= group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getId,
                MemberInfo::getName
        ));
        List<BalanceDto> balances = new ArrayList<>();

        for (Map.Entry<String, Double> entry : balanceMap.entrySet()) {

            String userId = entry.getKey();
            double netBalance = entry.getValue();

            String userName = userIdToName.get(userId);

            double amountToPay = 0.0;
            double amountToReceive = 0.0;

            if (netBalance > 0) {
                amountToReceive = netBalance;
            } else if (netBalance < 0) {
                amountToPay = Math.abs(netBalance);
            }

            balances.add(
                    BalanceDto.builder().user(userName).amountToPay(amountToPay).amountToReceive(amountToReceive).build()
            );
        }

            return balances;
    }
}
