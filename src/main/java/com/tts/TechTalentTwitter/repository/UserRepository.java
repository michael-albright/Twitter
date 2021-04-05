package com.tts.TechTalentTwitter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.TechTalentTwitter.model.User;

@Repository						//   store USERS; primary key is a long 
public interface UserRepository extends CrudRepository<User, Long>
{
	User findByUsername(String username);
}
