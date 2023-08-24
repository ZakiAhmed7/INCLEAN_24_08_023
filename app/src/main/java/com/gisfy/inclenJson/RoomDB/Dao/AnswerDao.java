package com.gisfy.inclenJson.RoomDB.Dao;

import com.gisfy.inclenJson.Entity.Answer;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AnswerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Answer answer);

    @Query("select * from Answers where surveyId=:surveyId")
    List<Answer> findAllAnswers(String surveyId);

    @Query("select count(*) from Answers where synced=:flag and surveyId=:surveyId")
    int countOfSynced(boolean flag,String surveyId);

    @Query("select count(*) from Answers where synced=:flag and surveyId=:surveyId")
    int countOfPartialSurvey(boolean flag,String surveyId);

    @Query("DELETE FROM Answers")
    void deleteAllv();
    @Delete
    void delete(Answer answer);

    @Update
    void update(Answer answer);

}
