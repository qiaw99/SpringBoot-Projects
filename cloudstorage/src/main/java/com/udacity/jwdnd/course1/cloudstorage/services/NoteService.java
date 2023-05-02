package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Note findNoteByID(Note note){
        Note temp = this.noteMapper.findNoteByID(note.getNoteid());
        return temp;
    }

    public boolean isNoteAvailable(Note note){
        Note temp = findNoteByID(note);
        if (temp != null){
            return true;
        } else {
            return false;
        }
    }

    public int updateNote(Note note){
        return this.noteMapper.updateNote(note);
    }

    public int addNote(Note note){
        if (note.getNoteid() != null) {
            if (isNoteAvailable(note)) {
                return updateNote(note);
            }
        }
        return this.noteMapper.addNote(new Note(null, note.getNotetitle(), note.getNotedescription(), note.getUserid()));
    }

    public List<Note> getAllNotes(){
        return this.noteMapper.getAllNotes();
    }

    public void deleteNote(Integer noteid){
        this.noteMapper.deleteNote(noteid);
    }
}
