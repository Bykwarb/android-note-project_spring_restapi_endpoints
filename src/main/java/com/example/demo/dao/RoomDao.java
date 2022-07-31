package com.example.demo.dao;

import com.example.demo.entity.Note;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;

import java.util.List;


public interface RoomDao {
    void createRoom(Room room);
    void deleteRoom(Room room);
    Room getRoomById(Long id);

    List<Note> getNotesFromRoom(Room room);
    void enterToRoom(User user, Room room);
    void deleteUserFromRoom(User user, Room room);
    void updateRoom(Room room);
    List<Room> getRoomsByCreator(User user);

    List<Room> getRoomsByUser(User user);
}
