package com.downtube.videos.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.downtube.videos.R;

public class FeedbackActivity extends AppCompatActivity implements View.OnTouchListener {
    private EditText descriptionEditText, emailEditText;
    private RadioButton suggestionRadioButton, problemRadioButton;
    private ScrollView mainScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initViews();
    }


    private void initViews() {
        descriptionEditText = (EditText) findViewById(R.id.feedback_edit_text);
        emailEditText = (EditText) findViewById(R.id.feedback_edit_text_mail);
        suggestionRadioButton = (RadioButton) findViewById(R.id.radioButtonSuggestion);
        problemRadioButton = (RadioButton) findViewById(R.id.radioButtonProblem);
        mainScrollView = (ScrollView) findViewById(R.id.scrollView);
        Button sendButton = (Button) findViewById(R.id.send_feedback);

        suggestionRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    problemRadioButton.setChecked(false);
                    suggestionRadioButton.setTextColor(getResources().getColor(R.color.colorBlue));
                    problemRadioButton.setTextColor(getResources().getColor(R.color.colorLightGrayText));
                } else {
                    suggestionRadioButton.setTextColor(getResources().getColor(R.color.colorLightGrayText));
                    problemRadioButton.setTextColor(getResources().getColor(R.color.colorBlue));
                    suggestionRadioButton.setChecked(false);
                }
            }
        });

        problemRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    suggestionRadioButton.setChecked(false);
                    problemRadioButton.setTextColor(getResources().getColor(R.color.colorBlue));
                    suggestionRadioButton.setTextColor(getResources().getColor(R.color.colorLightGrayText));
                } else {
                    suggestionRadioButton.setChecked(true);
                    suggestionRadioButton.setTextColor(getResources().getColor(R.color.colorBlue));
                    problemRadioButton.setTextColor(getResources().getColor(R.color.colorLightGrayText));
                }
            }
        });

        descriptionEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    descriptionEditText.setBackgroundResource(R.drawable.edit_text_feedback_blue);
                    descriptionEditText.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    descriptionEditText.setBackgroundResource(R.drawable.edit_text_feedback);
                }
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailEditText.setBackgroundResource(R.drawable.edit_text_feedback_blue);
                    emailEditText.setTextColor(getResources().getColor(android.R.color.black));
                } else {
                    emailEditText.setBackgroundResource(R.drawable.edit_text_feedback);
                }
            }
        });

        if (sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mainScrollView.scrollTo(0, mainScrollView.getBottom());
        return false;
    }


    public void onBackBtn(View view) {
        onBackPressed();
    }
}
