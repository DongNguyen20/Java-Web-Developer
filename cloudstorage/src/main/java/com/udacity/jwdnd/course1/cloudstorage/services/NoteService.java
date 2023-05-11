package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Note findOneById(Integer noteId){
        return noteMapper.findOneById(noteId);
    }

    public List<Note> findAllByUserId(Integer userId){
        return noteMapper.findAllByUserId(userId);
    }

    public int create(Note note){
        return noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }

    public int update(Integer noteId, Note note) {
        return noteMapper.update(noteId, note);
    }

    public int delete(Integer noteId){
        return noteMapper.delete(noteId);
    }
}
