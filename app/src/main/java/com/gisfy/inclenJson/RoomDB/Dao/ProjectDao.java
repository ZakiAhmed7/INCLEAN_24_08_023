package com.gisfy.inclenJson.RoomDB.Dao;

import com.gisfy.inclenJson.Entity.Program;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM Program")
    List<Program> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Program Program);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Program> Program);

    @Query("DELETE FROM Program WHERE projectGeneratedId = :id")
    void deleteById(int id);

    @Update
    void update(Program program);



}