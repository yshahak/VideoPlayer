package com.downtube.videos.activities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.downtube.videos.R;
import com.downtube.videos.Utils;
import com.startapp.android.publish.StartAppAd;

import java.util.ArrayList;
import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.downtube.videos.activities.MainActivity.CODE_STORAGE_PERMISSION;

/**
 * Created by B.E.L on 11/09/2016.
 */

public class DownloadDialogActivity extends AppCompatActivity implements View.OnClickListener , RadioGroup.OnCheckedChangeListener{
    public static final String EXTRA_VIDEOS_STREAMS_KEYS = "extraStreams";
    public static final String EXTRA_VIDEOS_STREAMS_LINKS = "extraLinks";
    public static final String EXTRA_VIDEO_TITLE = "extreVideoTitle";
    private static final String KEY_STARTAPP_COUNT = "keyStartAppCOunt";
    private static final String FACEBOOK_PLACEMENT_DOWNLOADED = "1671180859802010_1671181003135329";

//    private InterstitialAd  downloadPressedInterstital;
    private EditText editText;

    private TypedArray ids;
    private ArrayList<String> arrayStrames, arrayLinks   ;
    private int position;


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        downloadPressedInterstital = new InterstitialAd(this, FACEBOOK_PLACEMENT_DOWNLOADED);
//        downloadPressedInterstital.loadAd();
        setContentView(R.layout.dialog_download);
        editText = (EditText)findViewById(R.id.edit_text_file_name);
        ids = getResources().obtainTypedArray(R.array.menu_ids);
        arrayStrames = new ArrayList<>();
        String[] arr = getIntent().getStringArrayExtra(EXTRA_VIDEOS_STREAMS_KEYS);
        arrayStrames.addAll(new ArrayList<>(Arrays.asList(arr)));

        arrayLinks = new ArrayList<>();
        String[] arr2 = getIntent().getStringArrayExtra(EXTRA_VIDEOS_STREAMS_LINKS);
        arrayLinks.addAll(new ArrayList<>(Arrays.asList(arr2)));
        addRadioButtons();
        editText.setText(getIntent().getStringExtra(EXTRA_VIDEO_TITLE));

    }


    private void addRadioButtons() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        int id;
        for (int i = 0; i < arrayStrames.size(); i++) {
            id = ids.getResourceId(i, -1);
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setText(arrayStrames.get(i));
            rdbtn.setId(id);
            radioGroup.addView(rdbtn);
            if (i == 0) {
                rdbtn.setChecked(true);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_yes) {
            boolean permission = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (permission) {
                MainActivity.pathId = editText.getText().toString() + ".mp4";
                MainActivity.downId = Utils.downloadFile(getApplicationContext(), arrayLinks.get(position), MainActivity.pathId);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int count = pref.getInt(KEY_STARTAPP_COUNT, 0);
                if (count % 3 == 0) {
//                    if (downloadPressedInterstital.isAdLoaded()){
//                        downloadPressedInterstital.show();
//                    } else {
                        StartAppAd.showAd(this);
//                    }
                }
                pref.edit().putInt(KEY_STARTAPP_COUNT, ++count).apply();
                finish();
            } else {
                Toast.makeText(this, "You need to allow writing to memory", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_STORAGE_PERMISSION);
            }
        } else {
            finish();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        position = radioGroup.indexOfChild(radioGroup.findViewById(id));
        Log.d("TAG", arrayLinks.get(position));
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (downloadPressedInterstital != null) {
//            downloadPressedInterstital.destroy();
//        }

    }
}