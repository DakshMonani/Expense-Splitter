package com.daksh.expense_splitter.controller.setllmentservice;

import com.daksh.expense_splitter.dto.settlment.SettlementDto;
import com.daksh.expense_splitter.service.SettlementService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups/{groupId}/settlements")
public class SettlementServiceController {

    private final SettlementService settlementService;

    @GetMapping
    public ResponseEntity<List<SettlementDto>> getAllSettlements(@PathVariable String groupId){
        return ResponseEntity.ok(settlementService.calculateSettlements(groupId));
    }
}
