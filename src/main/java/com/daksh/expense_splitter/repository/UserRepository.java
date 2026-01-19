package com.daksh.expense_splitter.repository;

import com.daksh.expense_splitter.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends MongoRepository<User,String> {

    List<User> findByNameIn(List<String> memberNames);
}
