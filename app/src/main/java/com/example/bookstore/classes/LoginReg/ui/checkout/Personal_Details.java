package com.example.bookstore.classes.LoginReg.ui.checkout;

import static java.lang.Integer.parseInt;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bookstore.R;
import com.example.bookstore.classes.LoginReg.ui.basket.CommonFunctions;
import com.example.bookstore.classes.LoginReg.ui.store.StoreFragment;
import com.example.bookstore.classes.LoginReg.ui.store.book_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class Personal_Details extends AppCompatActivity {


    Button confirmOrder;

    RadioButton radioButton;
    RadioGroup payMethod;
    TextView fname,lname,email,telephone,address,postCode,city;


    //FIREBASE
    FirebaseAuth mAuth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference= db.getReference();
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    String usersId = user.getUid(); //get user's id

    ImageButton imageButton;

    //SET ARRAY LIST
    ArrayList<book_info> list;



    int count=0;
    SpeechRecognizer speechRecognizer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //SPEECH TO TEXT
        imageButton = findViewById(R.id.mic);
        email = findViewById(R.id.emailV);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        // check for permissions to user mic
        if(ContextCompat.checkSelfPermission(Personal_Details.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }



        imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_UP){
                    imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_off_24));

                    speechRecognizer.stopListening();
                    count=0;

                }

                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    imageButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_mic_24));


                    speechRecognizer.startListening(speechRecognizerIntent);
                    count = 1;
                }

                return false;
            }
        });

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

                Log.d("speech rec",String.valueOf(i));
            }

            @Override
            public void onResults(Bundle bundle) {

                ArrayList<String> data = bundle.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);

                email.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });






        // get number of specific books - and remove number of books from firebase
        confirmOrder = findViewById(R.id.buttonConfirm);
        confirmOrder.setOnClickListener(view -> {

            //PAYMENT SELECTION
            payMethod = (RadioGroup) findViewById(R.id.payMethod);
            int selectedId = payMethod.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            radioButton = (RadioButton) findViewById(selectedId);



            //PAY VIA CASH
            if (checkForNulls() && radioButton.getText().toString().equals("Cash On Delivery")){

                // GET BOOKS AND THEIR QUANTITY AND UPDATE THEM TO THE FIREBASE
                CommonFunctions callVar = new  CommonFunctions(this);


                //GET ADDED BOOKS
                list = callVar.loadData(true);


                DatabaseReference bookRef = reference.child("Books");


                //LOOP IN ADDED BOOKS
                for (int position = 0; position < list.size(); position++){




                    // UPDATE AVAILABILITY IN FIREBASE
                    bookRef.child(list.get(position).getName()).child("availability").setValue(Integer.toString(parseInt(list.get(position).getAvailability()) - parseInt(list.get(position).getQuantity())));


                }
                SharedPreferences sharedPreferences2 = this.getSharedPreferences("AddedBooks", MODE_PRIVATE);


                sharedPreferences2.edit().remove(usersId).commit(); //clear user's added books list


                Toast.makeText(this, "your order is finished and we are now processing it!", Toast.LENGTH_SHORT).show();


                //RETURN TO STORE
                startActivity(new Intent(Personal_Details.this, StoreFragment.class));


            // PAY VIA CARD
            }else if(checkForNulls() && radioButton.getText().toString().equals("Credit / Debit Card")) {

                startActivity(new Intent(Personal_Details.this, Card_details.class));


            //RADIO BUTTON NOT SELECTED
            }else if(radioButton.getText().toString() != "Cash On Delivery" &&  radioButton.getText().toString() != "Credit / Debit Card"){

                Toast.makeText(Personal_Details.this,"Please select a payment method!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public static final Integer RecordAudioRequestCode = 1;

    //check for permissions for mic
    private void checkPermission() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == RecordAudioRequestCode && grantResults.length>0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public boolean checkForNulls(){
        boolean empty = true;

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.emailV);
        telephone = findViewById(R.id.telephone);
        address = findViewById(R.id.address);
        postCode = findViewById(R.id.postCode);
        city = findViewById(R.id.city);


        if(fname.getText().toString().isEmpty() || lname.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||telephone.getText().toString().isEmpty() ||address.getText().toString().isEmpty() ||postCode.getText().toString().isEmpty() ||city.getText().toString().isEmpty() ){

            empty = false;

            Toast.makeText(Personal_Details.this,"Please fill all the fields!", Toast.LENGTH_SHORT).show();

        }

        return empty;
    }
}
