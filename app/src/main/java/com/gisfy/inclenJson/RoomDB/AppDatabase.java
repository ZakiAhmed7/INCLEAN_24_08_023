package com.gisfy.inclenJson.RoomDB;

import android.content.Context;

import com.gisfy.inclenJson.Entity.Answer;
import com.gisfy.inclenJson.Entity.Program;
import com.gisfy.inclenJson.Entity.Question;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormDatum;
import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormJsonAnswer;
import com.gisfy.inclenJson.RoomDB.Dao.AnswerDao;
import com.gisfy.inclenJson.RoomDB.Dao.ProjectDao;
import com.gisfy.inclenJson.RoomDB.Dao.QuestionDao;
import com.gisfy.inclenJson.RoomDB.Dao.SurveyDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Program.class, Question.class, Answer.class, SurveyFormDatum.class, SurveyFormJsonAnswer.class}, version = 4,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProjectDao projectDao();

    public abstract AnswerDao answerDao();

    public abstract QuestionDao questionDao();

    public abstract SurveyDao surveyDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getObInstance(Context context){
        INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,"InclenProject")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        return INSTANCE;
    }

}