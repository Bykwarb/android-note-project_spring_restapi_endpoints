package com.example.demo.dao;

import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Component;

import java.util.List;


public interface NoteDao {
    void createNote(Note note);
    void deleteNote(Note note);
    Note getNote(Long id);

    List<Note> getNotesByUser(User user);
    void updateNote(Note note);
}
