package com.gisfy.inclenJson.Utiles;

import com.gisfy.inclenJson.Entity.Answer;
import com.gisfy.inclenJson.RoomDB.Dao.AnswerDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class FormParser {

    public static JSONObject parseDataForSync(String json) throws JSONException {
        JSONObject requestBody = new JSONObject();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray fieldsArray = jsonObject.getJSONObject("step1").getJSONArray("fields");
        for (int i = 0 ; i<fieldsArray.length() ; i++){
            JSONObject fieldObject = fieldsArray.getJSONObject(i);
            if (!fieldObject.getString("type").equals("check_box")){
                requestBody.put(fieldObject.getString("dbColumn"),fieldObject.getString("value"));
            }else{
                JSONArray optionsArray = fieldObject.getJSONArray("options");
                JSONArray multiOptionAnswerArray = new JSONArray();
                for (int j = 0 ; j<optionsArray.length() ; j++){
                    multiOptionAnswerArray.put(optionsArray.getJSONObject(j).getString("value"));
                }
                requestBody.put(fieldObject.getString("dbColumn"),multiOptionAnswerArray);
            }
        }
        return requestBody;
    }

}
