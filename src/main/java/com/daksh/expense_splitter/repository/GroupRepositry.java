package com.daksh.expense_splitter.repository;

import com.daksh.expense_splitter.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepositry extends MongoRepository<Group,String> {
}
