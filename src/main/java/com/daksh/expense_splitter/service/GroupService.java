package com.daksh.expense_splitter.service;

import com.daksh.expense_splitter.dto.group.CreateGroupDto;
import com.daksh.expense_splitter.dto.group.GroupDto;
import com.daksh.expense_splitter.dto.group.UpdateGroupDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GroupService {

    public List<GroupDto> findAllGroups();

    GroupDto createGroup(CreateGroupDto createGroupDto);

    GroupDto findOne(String id);

    void deleteGroup(String id);

    GroupDto updatePartialGroup(String id, Map<String, Object> updates);

    GroupDto updateFullGroup(String id, UpdateGroupDto updateGroupDto);
}
