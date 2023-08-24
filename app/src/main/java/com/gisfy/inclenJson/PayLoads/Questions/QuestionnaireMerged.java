package com.gisfy.inclenJson.PayLoads.Questions;

import androidx.room.Embedded;
import androidx.room.Relation;

public class QuestionnaireMerged {
    @Embedded
    private Questionnaire questionnaire;
    @Relation(
    parentColumn = "dBColumn",
    entityColumn = "dBColumn")
    private Properties properties;

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public Properties getProperties() {
        return properties;
    }
}
