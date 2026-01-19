package com.daksh.expense_splitter.service.impl;

import com.daksh.expense_splitter.dto.group.CreateGroupDto;
import com.daksh.expense_splitter.dto.group.GroupDto;
import com.daksh.expense_splitter.dto.group.UpdateGroupDto;
import com.daksh.expense_splitter.helper.MemberInfo;
import com.daksh.expense_splitter.model.Group;
import com.daksh.expense_splitter.model.User;
import com.daksh.expense_splitter.repository.GroupRepositry;
import com.daksh.expense_splitter.repository.UserRepository;
import com.daksh.expense_splitter.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GroupServiceImplementation implements GroupService {

    private final GroupRepositry groupRepositry;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    @Override
    public List<GroupDto> findAllGroups() {
    return groupRepositry.findAll().stream().map(
        group -> {
            GroupDto dto = new GroupDto();
            dto.setId(group.getId());
            dto.setName(group.getName());
            List<String> names = group.getMembers().stream().map(MemberInfo::getName).toList();
            dto.setMemberName(names);
            return dto;
        }
    ).toList();

    }

    @Override
    public GroupDto createGroup(CreateGroupDto createGroupDto) {
      Group savedGroup = new Group();
      savedGroup.setName(createGroupDto.getName());
      List<String> memberNames = createGroupDto.getMemberName().stream().distinct().toList();
      List<User> users = userRepository.findByNameIn(memberNames);
        // validation
        if (users.size() != memberNames.size()) {
            throw new IllegalArgumentException("One or more users not found");
        }

        // convert
        List<MemberInfo> members = users.stream()
                .map(user -> new MemberInfo(user.getId(), user.getName()))
                .toList();

        savedGroup.setMembers(members);
        groupRepositry.save(savedGroup);
        List<String> names = savedGroup.getMembers().stream().map(MemberInfo::getName).toList();
        return GroupDto.builder().id(savedGroup.getId()).name(savedGroup.getName()).memberName(names).build();

    }

    @Override
    public GroupDto findOne(String id) {
        Group group = groupRepositry.findById(id).orElseThrow(()->new IllegalArgumentException("id with that group not found"));
        List<String> names = group.getMembers().stream().map(MemberInfo::getName).toList();
        return GroupDto.builder().id(group.getId()).name(group.getName()).memberName(names).build();
    }

    @Override
    public void deleteGroup(String id) {
        if (!groupRepositry.existsById(id)){
            throw new IllegalArgumentException("id not exists in database");
        }
        groupRepositry.deleteById(id);
    }

    @Override
    public GroupDto updatePartialGroup(String id, Map<String, Object> updates) {

        Group group = groupRepositry.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Only allow name update
        if (!updates.containsKey("name")) {
            throw new IllegalArgumentException("Only name can be updated");
        }

        // Do NOT allow member update
        if (updates.containsKey("members") || updates.containsKey("memberName")) {
            throw new IllegalArgumentException("Members cannot be updated using PATCH");
        }

        // Update name
        String newName = (String) updates.get("name");
        group.setName(newName);

        // Save
        Group updatedGroup = groupRepositry.save(group);

        // Convert members to names
        List<String> names = updatedGroup.getMembers()
                .stream()
                .map(MemberInfo::getName)
                .toList();

        return GroupDto.builder()
                .id(updatedGroup.getId())
                .name(updatedGroup.getName())
                .memberName(names)
                .build();
    }

    @Override
    public GroupDto updateFullGroup(String id, UpdateGroupDto updateGroupDto) {

        // 1. Find existing group
        Group group = groupRepositry.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // 2. Update name
        group.setName(updateGroupDto.getName());

        // 3. Remove duplicate member names
        List<String> memberNames = updateGroupDto.getMemberName()
                .stream()
                .distinct()
                .toList();

        // 4. Fetch users
        List<User> users = userRepository.findByNameIn(memberNames);

        // 5. Validate all users exist
        if (users.size() != memberNames.size()) {
            throw new IllegalArgumentException("One or more users not found");
        }

        // 6. Convert User -> MemberInfo
        List<MemberInfo> members = users.stream()
                .map(user -> new MemberInfo(user.getId(), user.getName()))
                .toList();

        // 7. Replace members completely
        group.setMembers(members);

        // 8. Save updated group
        Group updatedGroup = groupRepositry.save(group);

        // 9. Prepare response DTO
        List<String> names = updatedGroup.getMembers()
                .stream()
                .map(MemberInfo::getName)
                .toList();

        return GroupDto.builder()
                .id(updatedGroup.getId())
                .name(updatedGroup.getName())
                .memberName(names)
                .build();
    }


}
