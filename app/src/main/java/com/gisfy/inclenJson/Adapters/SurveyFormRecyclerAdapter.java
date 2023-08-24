package com.gisfy.inclenJson.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gisfy.inclenJson.PayLoads.SurveyEn.SurveyFormDatum;
import com.gisfy.inclenJson.RoomDB.AppDatabase;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import inclenJson.R;

public class SurveyFormRecyclerAdapter extends RecyclerView.Adapter {

    Context context;
    List<SurveyFormDatum> list = new ArrayList<>();

    public SurveyFormRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void updateList(List<SurveyFormDatum> list) {
        this.list = list;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case 0:
                view = layoutInflater.inflate(R.layout.item_text_type, parent, false);
                return new TextTypeViewHolder(view);
            case 1:
                view = layoutInflater.inflate(R.layout.item_text_number_type, parent, false);
                return new TextNumTypeViewHolder(view);
            case 2:
                view = layoutInflater.inflate(R.layout.item_two_option_type, parent, false);
                return new TrueFalseChoiceViewHolder(view);
            case 3:
                view = layoutInflater.inflate(R.layout.item_option_onechoice_type, parent, false);
                return new MultiTextOneChoiceViewHolder(view);
            case 4:
                view = layoutInflater.inflate(R.layout.item_option_multichoice_type, parent, false);
                return new MultiTextMultiChoiceViewHolder(view);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }


    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getQuestion_type().toLowerCase()) {
            case "text":
                return 0;
            case "number":
                return 1;
            case "trueorfalse":
                return 2;
            case "multichoiceoneans":
                return 3;
            case "multichoicemultians":
                return 4;
        }

        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        SurveyFormDatum item = list.get(position);

        switch (list.get(position).getQuestion_type().toLowerCase()) {
            case "text":
                TextTypeViewHolder textTypeViewHolder = (TextTypeViewHolder) holder;
                textTypeViewHolder.questionTv.setText((position + 1) + "." + item.getQuestion().toString());
                textTypeViewHolder.answerEditText.setText(item.getAnswer());

                RxTextView.textChanges(textTypeViewHolder.answerEditText)
                        .debounce(1, TimeUnit.SECONDS)
                        .subscribe(textChanged -> {
                           item.setUserAnswer(textTypeViewHolder.answerEditText.getText().toString());

                            AppDatabase.getObInstance(context).surveyDao().insert(item);
                        });
                break;
            case "number":
                TextNumTypeViewHolder textNumTypeViewHolder = (TextNumTypeViewHolder) holder;
                textNumTypeViewHolder.questionTv.setText((position + 1) + "." + item.getQuestion().toString());
                textNumTypeViewHolder.answerEditText.setText(item.getAnswer());

                RxTextView.textChanges(textNumTypeViewHolder.answerEditText)
                        .debounce(1, TimeUnit.SECONDS)
                        .subscribe(textChanged -> {
                           item.setUserAnswer(textNumTypeViewHolder.answerEditText.getText().toString());

                            AppDatabase.getObInstance(context).surveyDao().insert(item);
                        });
                break;
            case "trueorfalse":
                TrueFalseChoiceViewHolder trueFalseChoiceViewHolder = (TrueFalseChoiceViewHolder) holder;
                trueFalseChoiceViewHolder.questionTv.setText((position + 1) + "." + item.getQuestion().toString());
//                trueFalseChoiceViewHolder.answerEditText.setText(item.getAnswer());

                trueFalseChoiceViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        if (((RadioButton) radioGroup.findViewById(i)).isChecked()) {
                           item.setUserAnswer(((RadioButton) radioGroup.findViewById(i)).getText().toString());
                            AppDatabase.getObInstance(context).surveyDao().insert(item);
                        }
                    }
                });


                break;
            case "multichoiceoneans":
                MultiTextOneChoiceViewHolder multiTextOneChoiceViewHolder = (MultiTextOneChoiceViewHolder) holder;
                multiTextOneChoiceViewHolder.questionTv.setText((position + 1) + "." + item.getQuestion().toString());

                HashMap optionList = new Gson().fromJson(item.getAnswer(), HashMap.class);

                for (int i = 0; i < 4; i++) {
                    ((RadioButton) multiTextOneChoiceViewHolder.radioGroup.getChildAt(i)).setText(optionList.get("option" + (i + 1)).toString());
                }
//                multiTextOneChoiceViewHolder.answerEditText.setText(item.getAnswer());

                multiTextOneChoiceViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        if (((RadioButton) radioGroup.findViewById(i)).isChecked()) {
                           item.setUserAnswer(((RadioButton) radioGroup.findViewById(i)).getText().toString());
                            AppDatabase.getObInstance(context).surveyDao().insert(item);
                        }

                    }
                });


                break;
            case "multichoicemultians":
                MultiTextMultiChoiceViewHolder MultiTextMultiChoiceViewHolder = (MultiTextMultiChoiceViewHolder) holder;
                MultiTextMultiChoiceViewHolder.questionTv.setText((position + 1) + "." + item.getQuestion().toString());

                optionList = new Gson().fromJson(item.getAnswer(), HashMap.class);

                for (int i = 0; i < 4; i++) {
                    ((RadioButton) MultiTextMultiChoiceViewHolder.radioGroup.getChildAt(i)).setText(optionList.get("option" + (i + 1)).toString());
                }

                MultiTextMultiChoiceViewHolder.uncheckAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < MultiTextMultiChoiceViewHolder.radioGroup.getChildCount(); i++) {
                            ((RadioButton) MultiTextMultiChoiceViewHolder.radioGroup.getChildAt(i)).setChecked(false);
                        }
                       item.setUserAnswer("");
                        AppDatabase.getObInstance(context).surveyDao().insert(item);
                    }
                });

                MultiTextMultiChoiceViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        if (((RadioButton) radioGroup.findViewById(i)).isChecked()) {
                            if (item.getAnswer().contains(((RadioButton) radioGroup.findViewById(i)).getText().toString())) {
                                return;
                            }
                            String answer = item.getAnswer();
                            answer = answer + "," + ((RadioButton) radioGroup.findViewById(i)).getText().toString();
                           item.setUserAnswer(answer);
                            AppDatabase.getObInstance(context).surveyDao().insert(item);
                        } else {
                            String answer = item.getAnswer();
                            answer.replace("," + ((RadioButton) radioGroup.findViewById(i)).getText().toString(), "");
                           item.setUserAnswer(answer);
                            AppDatabase.getObInstance(context).surveyDao().insert(item);


                        }


                    }
                });

//                MultiTextMultiChoiceViewHolder.answerEditText.setText(item.getAnswer());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView questionTv;
        EditText answerEditText;

        public TextTypeViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTv = itemView.findViewById(R.id.question);
            answerEditText = itemView.findViewById(R.id.input);


        }
    }

    class TextNumTypeViewHolder extends RecyclerView.ViewHolder {

        TextView questionTv;
        EditText answerEditText;

        public TextNumTypeViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTv = itemView.findViewById(R.id.question);
            answerEditText = itemView.findViewById(R.id.input);

        }
    }

    class MultiTextOneChoiceViewHolder extends RecyclerView.ViewHolder {

        TextView questionTv;
        RadioGroup radioGroup;

        public MultiTextOneChoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTv = itemView.findViewById(R.id.question);
            radioGroup = itemView.findViewById(R.id.radio_group);

        }
    }


    class MultiTextMultiChoiceViewHolder extends RecyclerView.ViewHolder {

        TextView questionTv;
        RadioGroup radioGroup;
        Button uncheckAll;

        public MultiTextMultiChoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTv = itemView.findViewById(R.id.question);
            radioGroup = itemView.findViewById(R.id.radio_group);
            uncheckAll = itemView.findViewById(R.id.uncheck_all);

        }
    }


    class TrueFalseChoiceViewHolder extends RecyclerView.ViewHolder {

        TextView questionTv;
        RadioGroup radioGroup;


        public TrueFalseChoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTv = itemView.findViewById(R.id.question);
            radioGroup = itemView.findViewById(R.id.radio_group);


        }
    }
}
