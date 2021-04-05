package com.tts.TechTalentTwitter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.TechTalentTwitter.model.Role;

@Repository							// will store a Role, primary key is a long
public interface RoleRepository extends CrudRepository<Role, Long> 
{
	Role findByRole(String role);
}
