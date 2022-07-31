package com.example.demo.service;

import com.example.demo.dao.RoomDao;
import com.example.demo.entity.Note;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import com.example.demo.exception.UserAlreadyInTheRoomException;
import com.example.demo.exception.UserIsCreatorException;
import com.example.demo.exception.YouAreNotCreatorException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RoomService {
    private final RoomDao roomDao;
    private final UserService userService;
    public RoomService(RoomDao roomDao, UserService userService) {
        this.roomDao = roomDao;
        this.userService = userService;
    }

    public void createRoom(User user, String roomName){
        Room room = new Room();
        room.setCreator(user);
        room.getUsers().add(user);
        room.setRoomName(roomName);
        roomDao.createRoom(room);
    }
    public void updateRoom(Room room){
        roomDao.updateRoom(room);
    }

    public void deleteRoom(Room room){
        roomDao.deleteRoom(room);
    }

    public void enterToRoom(Long userId, Long roomId){
        Room room = getRoomById(roomId);
        User user = userService.getUserDataById(userId);
        if(room.getUsers().contains(user)){
            throw new UserAlreadyInTheRoomException("User already in the room!");
        }
        roomDao.enterToRoom(user, room);
    }

    public void exitFromRoom(Long userId, Long roomId){
        Room room = getRoomById(roomId);
        User user = userService.getUserDataById(userId);
        roomDao.deleteUserFromRoom(user, room);
    }

    public void kickUser(Long userId, Long roomId){
        User user = userService.getUserDataById(userId);
        Room room = getRoomById(roomId);
        if (user.equals(room.getCreator())){
            throw new UserIsCreatorException("User is creator!");
        }
        roomDao.deleteUserFromRoom(user, room);
    }
    public void changeCreator(Long userId, Long roomId, Long newCreatorId){
        User user = userService.getUserDataById(userId);
        User newCreator = userService.getUserDataById(newCreatorId);
        Room room = getRoomById(roomId);
        if (!user.equals(room.getCreator())){
            throw new YouAreNotCreatorException("You are not room creator");
        }
        room.setCreator(newCreator);
        roomDao.updateRoom(room);
    }

    public List<Note> getNotesFromRoom(Long roomId){
        Room room = getRoomById(roomId);
        if (Objects.isNull(room)){
            throw new NullPointerException();
        }
        return roomDao.getNotesFromRoom(room);
    }
    public User getCreator(Long roomId){
        Room room = getRoomById(roomId);
        if (Objects.isNull(room)){
            throw new NullPointerException();
        }
        return room.getCreator();
    }
    public Room getRoomById(Long id){
        return roomDao.getRoomById(id);
    }

    public List<User> getUsersFromRoom(Long roomId){
        Room room = getRoomById(roomId);
        if (Objects.isNull(room)){
            throw new NullPointerException();
        }
        return room.getUsers();
    }
    public List<Room> getRoomsByCreator(User creator){
        return roomDao.getRoomsByCreator(creator);
    }

    public List<Room> getRoomsByUser(Long id){
        List<Room> rooms = roomDao.getRoomsByUser(userService.getUserDataById(id));
        return rooms;
    }
}
