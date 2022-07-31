package com.example.demo.service;

import com.example.demo.dao.NoteDao;
import com.example.demo.dao.RoomDao;
import com.example.demo.dao.UserDao;
import com.example.demo.entity.Note;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class UserService {
    private final UserDao userDao;
    private final RoomDao roomDao;
    private final PasswordEncoder encoder;
    public UserService(UserDao userDao, RoomDao roomDao, PasswordEncoder encoder) {
        this.userDao = userDao;
        this.roomDao = roomDao;
        this.encoder = encoder;
    }

    public void createNewUser(String nickname, String email, String password){
        User user = new User();
        user.setNickName(nickname);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        userDao.createUser(user);
    }

    public void updateUser(User user){
        userDao.updateUser(user);
    }

    public void deleteUser(Long userId){
        User user = userDao.getUserById(userId);
        userDao.deleteUser(user);
    }

    public User getUserDataById(Long userId){
        return userDao.getUserById(userId);
    }

    public User getUserDataByEmail(String email){
        User user = userDao.getUserByEmail(email);
        return user;
    }
}
