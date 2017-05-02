package com.init.panj.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.init.panj.R;
import com.init.panj.clases.DatabaseHandler;
import com.init.panj.model.RecomendBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AndroidSQLiteTutorialActivity extends Activity {
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnSpeak= (ImageButton) findViewById(R.id.btnSpeak);
        txtSpeechInput= (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        DatabaseHandler db = new DatabaseHandler(this);
        
        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
//        db.addRecommend(new RecomendBean("genre1", "91000000001", "artist1", "albumname1"));
//        db.addRecommend(new RecomendBean("genre2", "91000000002", "artist2","albumname2"));
//        db.addRecommend(new RecomendBean("genre3", "91000000003", "artist3","albumname3"));
//
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<RecomendBean> recomendBeans = db.getAllRecommend();
 
        for (RecomendBean cn : recomendBeans) {
            String log = "Id: "+cn.getID()+" ,genre: " + cn.getGenre() + " ,Phone: " + cn.getPhoneNumber();
                // Writing Contacts to log
        Log.d("Name: ", log);
            Toast.makeText(AndroidSQLiteTutorialActivity.this,log, Toast.LENGTH_LONG).show();
           db.deleteRecommend(cn);
        }
//        RecomendBean ct=db.getRecommend(2);
     //   Log.d("onlyone", "" + ct.getName() + "," + ct.getPhoneNumber());

    }
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                }
                break;
            }

        }
    }

}