package com.example.demo.rest;

import com.example.demo.entity.Note;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import com.example.demo.exception.UserAlreadyInTheRoomException;
import com.example.demo.exception.UserIsCreatorException;
import com.example.demo.exception.YouAreNotCreatorException;
import com.example.demo.service.RoomService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/room")
public class RoomRestController {

    private final RoomService roomService;
    private final UserService userService;
    private final ObjectMapper mapper;

    public RoomRestController(RoomService roomService, UserService userService, ObjectMapper mapper) {
        this.roomService = roomService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @PutMapping("/create")
    public ResponseEntity<String> createNewRoom(@RequestParam String email, @RequestParam String roomName) throws JsonProcessingException {
        User user = userService.getUserDataByEmail(email);
        if (Objects.isNull(user)){
            return new ResponseEntity<>("User is not exist", HttpStatus.BAD_REQUEST);
        }
        roomService.createRoom(user, roomName);
        List<Room> rooms = (roomService.getRoomsByCreator(user));
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept();
        SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.serializeAllExcept();
        FilterProvider filters = new SimpleFilterProvider().addFilter("roomFilter", filter).addFilter("userFilter", userFilter);
        return ResponseEntity.ok("Room created: " + mapper.writer(filters).writeValueAsString(rooms.get(rooms.size()-1)));
    }

    @PutMapping("/enter/{roomId}")
    public ResponseEntity<String> enterToRoom(@PathVariable("roomId") Long roomId, @RequestParam Long userId){
        try {
            roomService.enterToRoom(userId, roomId);
        }catch (UserAlreadyInTheRoomException | NullPointerException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("User entered the room");
    }

    @DeleteMapping("/exit")
    public ResponseEntity<String> exitFromRoom(@RequestParam Long roomId, @RequestParam Long userId){
        try {
            roomService.exitFromRoom(userId, roomId);
        }catch (Exception e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("User left the room");
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<String> deleteRoom(@PathVariable("roomId") Long roomId, @RequestParam Long userId){
        Room room = roomService.getRoomById(roomId);
        if (Objects.isNull(room)){
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
        if(!room.getCreator().getId().equals(userId)){
            return ResponseEntity.status(206).body("You're not the creator");
        }
        roomService.deleteRoom(room);
        return ResponseEntity.ok("Room has been deleted!");
    }

    @DeleteMapping("/kick/user/{roomId}")
    public ResponseEntity<String> kickUserFromRoom(@PathVariable("roomId") Long roomId, @RequestParam Long userId){
        try {
            roomService.kickUser(userId, roomId);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }catch (UserIsCreatorException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("User has been kicked!");
    }

    @PostMapping("/change/creator")
    public ResponseEntity<String> changeRoomCreator(@RequestParam Long roomId,
                                                    @RequestParam Long userId,
                                                    @RequestParam Long newCreatorUserId){
        try {
            roomService.changeCreator(userId, roomId, newCreatorUserId);
        }catch (YouAreNotCreatorException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Creator changed");
    }

    @GetMapping("/get")
    public ResponseEntity<String> getRoomsByUser(@RequestParam Long userId) throws JsonProcessingException {
        List<Room> roomList;
        try {
            roomList = roomService.getRoomsByUser(userId);
        }catch (NullPointerException e){
            return new ResponseEntity<>("Rooms not found", HttpStatus.NOT_FOUND);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept(
                "creator",
                "users",
                "notes",
                "creator",
                "content",
                "creationDate");
        FilterProvider filters = new SimpleFilterProvider().addFilter("roomFilter", filter);
        return ResponseEntity.ok(mapper.writer(filters).writeValueAsString(roomList));
    }

    @GetMapping("/get/notes")
    public ResponseEntity<String> getNotesFromRoom(@RequestParam Long roomId) throws JsonProcessingException {
        List<Note> notes;
        try {
            notes = roomService.getNotesFromRoom(roomId);
        }catch (Exception e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        SimpleBeanPropertyFilter roomFilter = SimpleBeanPropertyFilter.serializeAllExcept();
        SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.serializeAllExcept(
                "nickName",
                "email",
                "registrationDate",
                "rooms"
                );
        SimpleBeanPropertyFilter noteFilter = SimpleBeanPropertyFilter.serializeAllExcept("" +
                "room");
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("roomFilter", roomFilter)
                .addFilter("userFilter", userFilter)
                .addFilter("noteFilter", noteFilter);
        return ResponseEntity.ok(mapper.writer(filters).writeValueAsString(notes));
    }

    @GetMapping("/get/users")
    public ResponseEntity<String> getUsersFromRoom(@RequestParam Long roomId) throws JsonProcessingException {
        List<User> users;
        try {
            users = roomService.getUsersFromRoom(roomId);
        }catch (Exception e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.serializeAllExcept(
                "email",
                "registrationDate",
                "rooms"
        );
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter", userFilter);
        return ResponseEntity.ok(mapper.writer(filterProvider).writeValueAsString(users));
    }
    @GetMapping("/get/creator")
    public ResponseEntity<String> getCreator(@RequestParam Long roomId) throws JsonProcessingException {
        User user;
        try {
            user = roomService.getCreator(roomId);
        }catch (NullPointerException e){
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        SimpleBeanPropertyFilter userFilter = SimpleBeanPropertyFilter.serializeAllExcept(
                "email",
                "registrationDate",
                "rooms"
        );
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("userFilter", userFilter);
        return ResponseEntity.ok(mapper.writer(filterProvider).writeValueAsString(user));
    }
}
