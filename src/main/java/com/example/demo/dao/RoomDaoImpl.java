package com.example.demo.dao;

import com.example.demo.entity.Note;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import com.example.demo.repository.RoomRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RoomDaoImpl implements RoomDao {

    private final RoomRepository repository;
    public RoomDaoImpl(RoomRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createRoom(Room room) {
        repository.save(room);
    }

    @Override
    public void deleteRoom(Room room) {
        repository.delete(room);
    }

    @Override
    public Room getRoomById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public List<Note> getNotesFromRoom(Room room) {
        return room.getNotes();
    }

    @Override
    public void enterToRoom(User user, Room room) {
        room.getUsers().add(user);
        repository.save(room);
    }

    @Override
    public void deleteUserFromRoom(User user, Room room) {
        room.getUsers().remove(user);
        repository.save(room);
    }

    @Override
    public void updateRoom(Room room) {
        repository.save(room);
    }

    @Override
    public List<Room> getRoomsByCreator(User user) {
        return repository.findAllByCreator(user);
    }

    @Override
    public List<Room> getRoomsByUser(User user) {
        return repository.findAllByUsersContains(user);
    }
}
