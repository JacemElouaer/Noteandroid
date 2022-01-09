package com.example.noteandroidapp.Database;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.noteandroidapp.Models.Notes;

import java.util.List;

@Dao
public interface MainDAO {

    @Insert(onConflict =  REPLACE)
    void insert(Notes notes);

    @Query("SELECT *  FROM notes order by ID desc")
    List<Notes> getAll();

    @Query("update notes set  title=:title ,  notes=:notes where ID =:id")
    void  update(int id ,  String  title  , String  notes);

    @Delete
    void  delete (Notes notes);

    @Query("UPDATE notes set pinned =:pin where ID=:id")
    void pin(int id ,  boolean pin);
}
