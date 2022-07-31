package com.example.demo.rest;

import com.example.demo.entity.User;
import com.example.demo.service.NoteService;
import com.example.demo.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/note")
public class NoteRestController {
    private final NoteService noteService;
    private final RoomService roomService;
    public NoteRestController(NoteService service, RoomService roomService) {
        this.noteService = service;

        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createNote(@RequestParam String note,
                                             @RequestParam Long roomId,
                                             @RequestParam Long userId){
        User user = new User();
        user.setId(userId);
        noteService.createNote(note, user, roomService.getRoomById(roomId));
        return ResponseEntity.ok("Note saved");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteNote(@RequestParam Long noteId){
        try {
            noteService.deleteMessage(noteId);
        }catch (NullPointerException e){
            return new ResponseEntity<>("Note not found", HttpStatus.NOT_FOUND);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Note deleted");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateNote(@RequestParam Long noteId, String content){
        try {
            noteService.changeNote(noteId, content);
        }catch (NullPointerException e){
            return new ResponseEntity<>("Note not found", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>("Something wrong", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Note is changed");
    }
}
