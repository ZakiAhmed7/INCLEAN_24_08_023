package com.gisfy.inclenJson.RoomDB.Dao;

import com.gisfy.inclenJson.Entity.Question;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM Question where surveyID=:surveyID Limit 1")
    Question selectProperty(String surveyID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question question);


    @Query("SELECT EXISTS(SELECT * FROM Question WHERE surveyID = :surveyId)")
    boolean isRowIsExist(String surveyId);

}
