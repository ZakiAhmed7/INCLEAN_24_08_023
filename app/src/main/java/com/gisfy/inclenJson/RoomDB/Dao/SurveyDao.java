package com.gisfy.inclenJson.RoomDB.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormDatum;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormJsonAnswer;

import java.util.List;


@Dao
public interface SurveyDao {


    @Query("SELECT * FROM SurveyFormDatum where surveyID = :surveyID")
    List<SurveyFormDatum> getAll(int surveyID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SurveyFormDatum surveyFormDatum);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertJsonAnswersForSync(SurveyFormJsonAnswer surveyFormJsonAnswer);

    @Query("Select count(*) from surveyformjsonanswer")
    int getSurveyToBeSyncCount();

    @Query("Select * from surveyformjsonanswer")
    List<SurveyFormJsonAnswer> getSurveyFormAnswers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SurveyFormDatum> surveyFormData);

    @Query("DELETE FROM SurveyFormDatum WHERE surveyID = :surveyID")
    void deleteById(int surveyID);

    @Query("DELETE FROM surveyformjsonanswer")
    void deleteAll();

    @Update
    void update(SurveyFormDatum surveyFormDatum);


}

