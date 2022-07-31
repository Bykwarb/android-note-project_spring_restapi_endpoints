package com.example.demo.dao;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao{
    private final UserRepository repository;

    public UserDaoImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createUser(User user) {
        repository.save(user);

    }

    @Override
    public void deleteUser(User user) {
        repository.delete(user);
    }

    @Override
    public User getUserById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void updateUser(User user) {
        repository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
       User user = repository.findByEmail(email);
       return user;
    }
}
