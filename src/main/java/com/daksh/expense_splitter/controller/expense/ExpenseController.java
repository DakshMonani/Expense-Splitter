package com.daksh.expense_splitter.controller.expense;

import com.daksh.expense_splitter.dto.expense.CreateExpenseDto;
import com.daksh.expense_splitter.dto.expense.ExpenseDto;
import com.daksh.expense_splitter.dto.expense.UpdateExpenseDto;
import com.daksh.expense_splitter.model.Expense;
import com.daksh.expense_splitter.service.ExpenseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @GetMapping("/{groupId}/expenses")
    public ResponseEntity<List<ExpenseDto>> findAll(@PathVariable String groupId){
        return ResponseEntity.ok(expenseService.findAllExpense(groupId));
    }
    @GetMapping("/{groupId}/expenses/{expenseId}")
    public  ResponseEntity<ExpenseDto> findOne(@PathVariable String groupId,@PathVariable String expenseId){
        return ResponseEntity.ok(expenseService.findOneExpense(groupId,expenseId));
    }

    @PostMapping("/{groupId}/expenses")
    public ResponseEntity<ExpenseDto> createExpense(@PathVariable String groupId, @RequestBody CreateExpenseDto createExpenseDto){
       return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createExpense(groupId,createExpenseDto));
    }
    @DeleteMapping("/{groupId0}/expense/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String groupId,@PathVariable String expenseId){
        expenseService.deleteExpense(groupId,expenseId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{groupId}/expense/{expenseId}")
    public ResponseEntity<ExpenseDto>  updateFullExpense(@PathVariable String groupId, @PathVariable String expenseId, @RequestBody UpdateExpenseDto updateExpenseDto){

      return ResponseEntity.ok(expenseService.updateFullUser(groupId,expenseId,updateExpenseDto));
    }
    @PatchMapping("/{groupId}/expense/{expenseId}")
    public  ResponseEntity<ExpenseDto> updatePartialExpense(@PathVariable String groupId , @PathVariable String expenseId , @RequestBody UpdateExpenseDto updateExpenseDto){
        return ResponseEntity.ok(expenseService.updatePartialExpense(groupId,expenseId,updateExpenseDto));
    }

}
