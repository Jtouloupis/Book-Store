package com.example.bookstore.classes.LoginReg.ui.store;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.R;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class detailsAct extends AppCompatActivity
{


    TextView name,price,availability,details;
    ImageView image;
    TextToSpeech detailsSpeak;
    Button detailsButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            name = (TextView) findViewById(R.id.nameDet);
            price = (TextView) findViewById(R.id.priceDet);
            availability = (TextView) findViewById(R.id.availabilityDet);
            details = (TextView) findViewById(R.id.detailsDet);
            detailsButton = (Button) findViewById(R.id.text2spee);
            image = (ImageView) findViewById(R.id.imageDet);



            //get the info of the selected book
            String name2 = extras.getString("name");
            String price2 = extras.getString("price");
            String availability2 = extras.getString("availability");
            String details2 = extras.getString("details");


            //set book details activity
            name.setText("Title: "+name2);
            price.setText("Price:"+ price2);
            availability.setText("Availability: "+availability2);
            details.setText(details2);
            Picasso.get().load(extras.getString("image")).into(image);


        }


        detailsSpeak = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                if (i == TextToSpeech.SUCCESS) {
                    int result = detailsSpeak.setLanguage(Locale.ENGLISH);

                    detailsSpeak.setPitch(0.5f);
                    detailsSpeak.setSpeechRate(0.5f);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    }
                } else {
                    Log.e("TTS", "Initialization failed!");
                }
            }});



        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = details.getText().toString();

                detailsSpeak.speak(text, TextToSpeech.QUEUE_FLUSH, null);

            }
        });

    }



    @Override
    protected void onDestroy() {

        if(detailsSpeak != null){
            detailsSpeak.stop();
            detailsSpeak.shutdown();
        }

        super.onDestroy();
    }
}
