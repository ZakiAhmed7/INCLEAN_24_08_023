package com.gisfy.inclenJson.PayLoads.Questions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Questionnaire {

    @SerializedName("DBColumn")
    private String dBColumn;
    @SerializedName("Question")
    private String question;
    @SerializedName("Question Type")
    private String questionType;
    @SerializedName("Field Type")
    private String fieldType;
    @SerializedName("Regexp")
    private String Regexp;
    @SerializedName("Options")
    private List<String> options;
    @SerializedName("Properties")
    private Properties properties;


    public Questionnaire() {
    }

    public String getRegexp() {
        return Regexp;
    }

    public void setRegexp(String regexp) {
        Regexp = regexp;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDBColumn() {
        return dBColumn;
    }

    public void setDBColumn(String dBColumn) {
        this.dBColumn = dBColumn;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
