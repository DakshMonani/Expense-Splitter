package com.daksh.expense_splitter.service;

import com.daksh.expense_splitter.dto.expense.CreateExpenseDto;
import com.daksh.expense_splitter.dto.expense.ExpenseDto;
import com.daksh.expense_splitter.dto.expense.UpdateExpenseDto;
import com.daksh.expense_splitter.model.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpenseService {
    List<ExpenseDto> findAllExpense(String groupId);

    ExpenseDto findOneExpense(String groupId, String expenseId);


    ExpenseDto createExpense(String groupId, CreateExpenseDto createExpenseDto);

    void deleteExpense(String groupId, String expenseId);

    ExpenseDto updateFullUser(String groupId, String expenseId, UpdateExpenseDto updateExpenseDto);

    ExpenseDto updatePartialExpense(String groupId, String expenseId, UpdateExpenseDto updateExpenseDto);
}
