package com.skcc.cna.sbtemplate.repository;

import org.springframework.data.repository.CrudRepository;
import com.skcc.cna.sbtemplate.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

}