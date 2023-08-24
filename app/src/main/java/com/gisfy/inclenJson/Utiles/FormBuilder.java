package com.gisfy.inclenJson.Utiles;

import android.content.Context;

import com.gisfy.inclenJson.PayLoads.Questions.Questionnaire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FormBuilder {

    private Context context;
    private JSONArray wholeArray = new JSONArray();
    public FormBuilder(Context context){
        this.context=context;

    }

    public void addData(Questionnaire question){
        switch (question.getQuestionType()){
            case Constants.MULTI_SELECT_TYPEFACE:
                addMultiSpinner(question);
                break;
            case Constants.TEXT_TYPEFACE:
                addEditText(question);
                break;

            case Constants.NUM_TYPEFACE:
                addEditText(question);
                break;

            case Constants.SINGLE_SELECT_TYPEFACE:
                addSpinner(question);
                break;

            case Constants.TRUE_OR_FALSE_TYPEFACE:
                addRadioButton(question);
                break;
        }
    }
    public void addEditText(Questionnaire question){

        try {
            JSONObject parent = new JSONObject();
            parent.put("key",question.getDBColumn());
            parent.put("type","edit_text");
            parent.put("hint",question.getQuestion());
            parent.put("hint",question.getQuestion());
            parent.put("dbColumn",question.getDBColumn());

            JSONObject v_max_length = new JSONObject();
            if (question.getProperties().getSize()!=null)
                v_max_length.put("value",question.getProperties().getSize());
            v_max_length.put("err","Max length can be at most "+question.getProperties().getSize()+".");
            parent.put("v_max_length",v_max_length);

            JSONObject v_min_length = new JSONObject();
            v_min_length.put("value","3");
            v_min_length.put("err","Min length should be at least 3");
            parent.put("v_min_length",v_min_length);

            JSONObject v_regex = new JSONObject();
            v_regex.put("value",question.getRegexp());
            v_regex.put("err","Not in proper format");

            parent.put("v_regex",v_regex);

            wholeArray.put(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addRadioButton(Questionnaire question){

        try {
            JSONObject parent = new JSONObject();
            parent.put("key",question.getDBColumn());
            parent.put("type","radio");
            parent.put("label",question.getQuestion());
            parent.put("dbColumn",question.getDBColumn());

            JSONArray options = new JSONArray();
            for (String option:question.getOptions()){
                JSONObject optionObj = new JSONObject();
                optionObj.put("key",option.replace(" ","_").toLowerCase());
                optionObj.put("text",option);
                options.put(optionObj);
            }
            parent.put("options",options);

            wholeArray.put(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSpinner(Questionnaire question){

        try {
            JSONObject parent = new JSONObject();
            parent.put("key",question.getDBColumn());
            parent.put("type","spinner");
            parent.put("label",question.getQuestion());
            parent.put("dbColumn",question.getDBColumn());

            JSONArray values = new JSONArray();
            for (String option:question.getOptions()){
                values.put(option);
            }
            parent.put("values",values);

            JSONObject v_required = new JSONObject();
            v_required.put("value","true");
            v_required.put("err","Please choose a value to proceed.");
            parent.put("v_required",v_required);

            wholeArray.put(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addMultiSpinner(Questionnaire question){

        try {
            JSONObject parent = new JSONObject();
            parent.put("key",question.getDBColumn());
            parent.put("type","check_box");
            parent.put("label",question.getQuestion());
            parent.put("dbColumn",question.getDBColumn());

            JSONArray options = new JSONArray();
            for (String option:question.getOptions()){
                JSONObject optionObj = new JSONObject();
                optionObj.put("key",option.replace(" ","_").toLowerCase());
                optionObj.put("text",option);
                optionObj.put("value","false");
                options.put(optionObj);
            }
            parent.put("options",options);

            JSONObject v_required = new JSONObject();
            v_required.put("value","true");
            v_required.put("err","Please choose a value to proceed.");
            parent.put("v_required",v_required);

            wholeArray.put(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONObject getForm(String title){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("count","1");
            JSONObject step1Obj = new JSONObject();
            step1Obj.put("title",title);
            step1Obj.put("fields",wholeArray);
            jsonObject.put("step1",step1Obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
