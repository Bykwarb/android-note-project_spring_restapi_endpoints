package com.example.demo.dao;

import com.example.demo.entity.Note;
import com.example.demo.entity.User;
import com.example.demo.repository.NoteRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteDaoImpl implements NoteDao{

    private final NoteRepository repository;

    public NoteDaoImpl(NoteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createNote(Note note) {
        repository.save(note);
    }

    @Override
    public void deleteNote(Note note) {
        repository.delete(note);
    }

    @Override
    public Note getNote(Long id) {
       return repository.findById(id).get();
    }

    @Override
    public List<Note> getNotesByUser(User user) {
        return repository.getNotesByCreator(user);
    }

    @Override
    public void updateNote(Note note) {
        repository.save(note);
    }
}
