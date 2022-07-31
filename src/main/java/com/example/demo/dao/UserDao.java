package com.example.demo.dao;

import com.example.demo.entity.User;

public interface UserDao {
    void createUser(User user);
    void deleteUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    User getUserByEmail(String email);

}
