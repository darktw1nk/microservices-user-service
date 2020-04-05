package com.winter.service;

import com.winter.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> listAll();

    Optional<User> findById(Long id);

    Optional<User> findByLogin(String login);

    Optional<User> save(User user);

    Optional<User> registerUser(String login,String password,String companyName);
}
