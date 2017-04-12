package com.github.chiby.store.model.repositories;

import org.springframework.data.repository.CrudRepository;

import com.github.chiby.player.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	User findOneByNickname(String nickname);
}