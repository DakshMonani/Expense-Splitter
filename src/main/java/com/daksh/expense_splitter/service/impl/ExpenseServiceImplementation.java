package com.daksh.expense_splitter.service.impl;

import com.daksh.expense_splitter.dto.expense.CreateExpenseDto;
import com.daksh.expense_splitter.dto.expense.ExpenseDto;
import com.daksh.expense_splitter.dto.expense.UpdateExpenseDto;
import com.daksh.expense_splitter.helper.MemberInfo;
import com.daksh.expense_splitter.model.Expense;
import com.daksh.expense_splitter.model.Group;
import com.daksh.expense_splitter.repository.ExpenseRepositry;
import com.daksh.expense_splitter.repository.GroupRepositry;
import com.daksh.expense_splitter.repository.UserRepository;
import com.daksh.expense_splitter.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImplementation implements ExpenseService {
    private final ExpenseRepositry expenseRepositry;
    private final ModelMapper  modelMapper;
    private final UserRepository userRepository;
    private final GroupRepositry groupRepositry;

    @Override
    public List<ExpenseDto> findAllExpense(String groupId) {
        Group group = groupRepositry.findById(groupId).orElseThrow(()-> new IllegalArgumentException("group not found"));

        List<Expense> expenses = expenseRepositry.findByGroupId(groupId);

        Map<String,String> userIdToName= group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getId,
                MemberInfo::getName
        ));
       return expenses.stream().map(
               expense -> {
                   return ExpenseDto.builder().id(expense.getId()).groupId(groupId).paidBy(userIdToName.get(expense.getPaidBy())).amount(expense.getAmount()).splitAmong(expense.getSplitAmong().stream().map(userIdToName::get).toList()).description(expense.getDescription()).category(expense.getCategory()).timestamp(expense.getTimestamp()).createdAt(expense.getCreatedAt()).updatedAt(expense.getUpdatedAt()).build();
               }
       ).toList();
    }

    @Override
    public ExpenseDto findOneExpense(String groupId, String expenseId) {
        Expense expense = expenseRepositry.findById(expenseId).orElseThrow(()->new IllegalArgumentException("expense with that id not exists"));

        Group group = groupRepositry.findById(groupId).orElseThrow(()->new IllegalArgumentException("group not found"));

        if (!expense.getGroupId().equals(groupId)){
            throw new IllegalArgumentException("Expense does not belong to this group");
        }

        Map<String,String> userIdToName= group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getId,
                MemberInfo::getName
        ));

        String paidBy = userIdToName.get(expense.getPaidBy());

        List<String> splitNames = expense.getSplitAmong().stream().map(userIdToName::get).toList();
        
        return ExpenseDto.builder().id(expense.getId()).groupId(groupId).paidBy(paidBy).amount(expense.getAmount()).splitAmong(splitNames).description(expense.getDescription()).category(expense.getCategory()).timestamp(expense.getTimestamp()).createdAt(expense.getCreatedAt()).updatedAt(expense.getUpdatedAt()).build();
    }

    @Override
    public ExpenseDto createExpense(String groupId, CreateExpenseDto createExpenseDto) {
        //checking that groupid group exists or not
        Group group = groupRepositry.findById(groupId).orElseThrow(()->new IllegalArgumentException("group not found"));

        //name-->id mapping
        Map<String, String> userNameToId = group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getName,
                MemberInfo::getId
        ));


        //id-->name mapping
        Map<String, String> userIdToName = group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getId,
                MemberInfo::getName
        ));

        //validating that names should exists in group also
        if (!userNameToId.containsKey(createExpenseDto.getPaidBy())) {
            throw new IllegalArgumentException("paidBy user not part of group");
        }

        //mapping name --> id by nametoid map
        String paidById = userNameToId.get(createExpenseDto.getPaidBy());

        List<String> splitAmongIds = new ArrayList<>();

        //validate that name exists in name-id map or not
        for (String name : createExpenseDto.getSplitAmong()) {

            if (!userNameToId.containsKey(name)) {
                throw new IllegalArgumentException("User " + name + " not part of group");
            }

            splitAmongIds.add(userNameToId.get(name)); // ✅ adds USER ID
        }
            if (!splitAmongIds.contains(paidById)){
                throw new IllegalArgumentException("that paidById not exists in splitAmongIds");
            }
        //creating expense from createexpensedto

        if (createExpenseDto.getTimestamp() == null){
            createExpenseDto.setTimestamp(Instant.now());
        }
        Expense expense = Expense.builder().groupId(groupId).paidBy(paidById).amount(createExpenseDto.getAmount()).splitAmong(splitAmongIds).description(createExpenseDto.getDescription()).category(createExpenseDto.getCategory()).timestamp(createExpenseDto.getTimestamp()).createdAt(Instant.now()).updatedAt(Instant.now()).build();


        //saving expense
        Expense newExpense = expenseRepositry.save(expense);

        //map paidbyid--->paidbynames
        String paidByName = userIdToName.get(newExpense.getPaidBy());


        //map id--->names
        List<String> splitAmongNames = newExpense.getSplitAmong().stream().map(userIdToName::get).toList();

        //conversion from expense ---> expensedto
        return ExpenseDto.builder().id(newExpense.getId()).groupId(newExpense.getGroupId()).paidBy(paidByName).amount(newExpense.getAmount()).splitAmong(splitAmongNames).description(newExpense.getDescription()).category(newExpense.getCategory()).timestamp(newExpense.getTimestamp()).createdAt(newExpense.getCreatedAt()).updatedAt(newExpense.getUpdatedAt()).build();



    }

    @Override
    public void deleteExpense(String groupId, String expenseId) {
        Group group = groupRepositry.findById(groupId).orElseThrow(()->new IllegalArgumentException("group not found"));

        Expense expense = expenseRepositry.findById(expenseId).orElseThrow(()->new IllegalArgumentException("expense with that id not exists"));
        if (!expense.getGroupId().equals(groupId)) {
            throw new IllegalArgumentException("Expense does not belong to this group");
        }

        expenseRepositry.delete(expense);
    }

    @Override
    public ExpenseDto updateFullUser(String groupId, String expenseId, UpdateExpenseDto updateExpenseDto) {
        Group group = groupRepositry.findById(groupId).orElseThrow(()->new IllegalArgumentException("group not found"));
        Map<String, String> userNameToId = group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getName,
                MemberInfo::getId
        ));


        //id-->name mapping
        Map<String, String> userIdToName = group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getId,
                MemberInfo::getName
        ));


        Expense expense = expenseRepositry.findById(expenseId).orElseThrow(()->new IllegalArgumentException("expense with that id not exists"));

        if (!expense.getGroupId().equals(groupId)) {
            throw new IllegalArgumentException("Expense does not belong to this group");
        }


        if (!userNameToId.containsKey(updateExpenseDto.getPaidBy())){
            throw new IllegalArgumentException("paidBy user not part of group");
        }
        String paidById = userNameToId.get(updateExpenseDto.getPaidBy());

        List<String> splitAmongIds =new ArrayList<>();

        for (String name : updateExpenseDto.getSplitAmong()){
            if (!userNameToId.containsKey(name)){
                throw new IllegalArgumentException("User " + name + " not part of group");
            }
            splitAmongIds.add(userNameToId.get(name));
        }
        if (!splitAmongIds.contains(paidById)){
            throw new IllegalArgumentException("paidBy must be part of splitAmong");
        }



        Instant timestamp = updateExpenseDto.getTimestamp() != null
                ? updateExpenseDto.getTimestamp()
                : Instant.now();

        expense.setPaidBy(paidById);
        expense.setAmount(updateExpenseDto.getAmount());
        expense.setSplitAmong(splitAmongIds);
        expense.setDescription(updateExpenseDto.getDescription());
        expense.setCategory(updateExpenseDto.getCategory());
        expense.setTimestamp(timestamp);
        expense.setUpdatedAt(Instant.now());


        Expense updatedExpense = expenseRepositry.save(expense);
        return ExpenseDto.builder().id(expenseId).groupId(groupId).paidBy(userIdToName.get(updatedExpense.getPaidBy())).amount(updatedExpense.getAmount()).splitAmong(updatedExpense.getSplitAmong().stream().map(userIdToName::get).toList()).description(updatedExpense.getDescription()).category(updatedExpense.getCategory()).timestamp(updatedExpense.getTimestamp()).createdAt(updatedExpense.getCreatedAt()).updatedAt(updatedExpense.getUpdatedAt()).build();

    }

    @Override
    public ExpenseDto updatePartialExpense(String groupId, String expenseId, UpdateExpenseDto updateExpenseDto) {
        Group group = groupRepositry.findById(groupId).orElseThrow(() -> new IllegalArgumentException("id with that group not found"));

        Expense expense = expenseRepositry.findById(expenseId).orElseThrow(() -> new IllegalArgumentException("expense with that id not found "));

        if (!expense.getGroupId().equals(groupId)) {
            throw new IllegalArgumentException("Expense does not belong to this group");
        }
        Map<String, String> userNameToId = group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getName,
                MemberInfo::getId
        ));


        //id-->name mapping
        Map<String, String> userIdToName = group.getMembers().stream().collect(Collectors.toMap(
                MemberInfo::getId,
                MemberInfo::getName
        ));

        String paidById = expense.getPaidBy(); // default (existing)

        if (updateExpenseDto.getPaidBy() != null) {
            if (!userNameToId.containsKey(updateExpenseDto.getPaidBy())) {
                throw new IllegalArgumentException("paidBy user not part of group");
            }
            paidById = userNameToId.get(updateExpenseDto.getPaidBy());
        }



        List<String> splitAmongByIds = expense.getSplitAmong(); // default

        if (updateExpenseDto.getSplitAmong() != null) {
            if (updateExpenseDto.getSplitAmong().isEmpty()) {
                throw new IllegalArgumentException("splitAmong cannot be empty");
            }
            splitAmongByIds = new ArrayList<>();

            for (String name : updateExpenseDto.getSplitAmong()) {
                if (!userNameToId.containsKey(name)) {
                    throw new IllegalArgumentException("user " + name + " not part of group");
                }
                splitAmongByIds.add(userNameToId.get(name));
            }
        }

        expense.setSplitAmong(splitAmongByIds);

        if (!splitAmongByIds.contains(paidById)){
            throw new IllegalArgumentException("paidBy must be part of splitAmong");
        }
        expense.setPaidBy(paidById);

        if (updateExpenseDto.getAmount() != null){
            expense.setAmount(updateExpenseDto.getAmount());
        }
        if (updateExpenseDto.getDescription() != null){
            expense.setDescription(updateExpenseDto.getDescription());
        }
        if (updateExpenseDto.getCategory() != null){
            expense.setCategory(updateExpenseDto.getCategory());
        }

        expense.setUpdatedAt(Instant.now());

        Expense saved = expenseRepositry.save(expense);

        return ExpenseDto.builder()
                .id(saved.getId())
                .groupId(groupId)
                .paidBy(userIdToName.get(saved.getPaidBy()))
                .amount(saved.getAmount())
                .splitAmong(saved.getSplitAmong().stream().map(userIdToName::get).toList())
                .description(saved.getDescription())
                .category(saved.getCategory())
                .timestamp(saved.getTimestamp())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();

    }


}
