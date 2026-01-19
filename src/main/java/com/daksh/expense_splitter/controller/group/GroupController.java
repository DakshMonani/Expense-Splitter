package com.daksh.expense_splitter.controller.group;

import com.daksh.expense_splitter.dto.group.CreateGroupDto;
import com.daksh.expense_splitter.dto.group.GroupDto;
import com.daksh.expense_splitter.dto.group.UpdateGroupDto;
import com.daksh.expense_splitter.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupDto>> findAll(){
        return ResponseEntity.ok(groupService.findAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> findOne(@PathVariable String id){
        return ResponseEntity.ok(groupService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody CreateGroupDto createGroupDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.createGroup(createGroupDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable String id){
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<GroupDto> updatePartialGroup(@PathVariable String id, @RequestBody Map<String, Object> updates){
        return ResponseEntity.ok(groupService.updatePartialGroup(id,updates));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDto> updateFullGroup(@PathVariable String id,@Valid  @RequestBody UpdateGroupDto updateGroupDto){
        return ResponseEntity.ok(groupService.updateFullGroup(id,updateGroupDto));
    }

}
