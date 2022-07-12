package com.example.note.Database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.note.Model.Note;

import java.util.List;

@Dao
public interface Main {

    @Insert(onConflict = REPLACE)
    void insert(Note note);

    @Query("SELECT * FROM note ORDER BY id DESC")
    List<Note> getAll;

    @Query("UPDATE note SET title = :title, note = :note WHERE ID = :id")
    void update(int id, String title, String note);


    @Delete
    void delete(Note note);


    void updete(int id, String title, String note);

    List<Note> getAll();
    @Query("UPDATE note SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);
}
