package com.downtube.videos.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.downtube.videos.R;
import com.downtube.videos.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.downtube.videos.activities.DownloadDialogActivity.EXTRA_VIDEO_TITLE;

/**
 * Created by B.E.L on 11/09/2016.
 */

public class DeleteDialogActivity extends AppCompatActivity implements View.OnClickListener{


    public static final String EXTRA_PATH = "extraPath";
    public static final String EXTRA_URI = "extreUri";
    private TextView textViewLabel;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete);
        textViewLabel = (TextView) findViewById(R.id.text_file_name);
        textViewLabel.setText(getIntent().getStringExtra(EXTRA_VIDEO_TITLE));

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            if (((TextView)v).getText().equals("OK")){
                setResult(RESULT_OK);
                finish();
                return;
            }
            String path = getIntent().getStringExtra(EXTRA_PATH);
            boolean deleted = Utils.deleteFile(path);
            Uri uri = Uri.parse(getIntent().getStringExtra(EXTRA_URI));
            getContentResolver().delete(uri, null, null);
            textViewLabel.setText(getString(R.string.delete_completed));
            ((TextView)v).setText("OK");
            (findViewById(R.id.btn_no)).setVisibility(View.GONE);
            (findViewById(R.id.text_sure)).setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }




}