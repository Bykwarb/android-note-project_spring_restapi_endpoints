package com.example.demo.repository;

import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByCreator(User creator);
    List<Room> findAllByUsersContains(User user);
}
