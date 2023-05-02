package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    public int addNote(Note note);

    @Select("SELECT * FROM NOTES")
    public List<Note> getAllNotes();

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid}")
    public Note findNoteByID(Integer noteid);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription}\n" +
            "WHERE noteid = #{noteid}")
    public int updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    public void deleteNote(Integer noteid);
}
