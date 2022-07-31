package com.example.demo.service;

import com.example.demo.dao.NoteDao;
import com.example.demo.dao.NoteDaoImpl;
import com.example.demo.entity.Note;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NoteService {
    private final NoteDao noteDao;
    private final RoomService service;
    public NoteService(NoteDao noteDao, RoomService service) {
        this.noteDao = noteDao;
        this.service = service;
    }

    public void createNote(String message, User creator, Room room){
        Note note = new Note();
        note.setContent(message);
        note.setCreator(creator);
        note.setRoom(room);
        note.setChanged(false);
        noteDao.createNote(note);
    }

    public void deleteMessage(Long messageId){
        noteDao.deleteNote(noteDao.getNote(messageId));
    }

    public void changeNote(Long messageId, String content){
        Note note  = noteDao.getNote(messageId);
        if (Objects.isNull(note)){
            throw new NullPointerException();
        }
        note.setContent(content);
        note.setChanged(true);
        noteDao.updateNote(note);
    }

    public List<Note> getNoteByUser(User user){
        return noteDao.getNotesByUser(user);
    }
}

