package com.example.bookstore.classes.LoginReg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity  {


    FirebaseAuth mAuth;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    String usersId = user.getUid(); //get user's id
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference= db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_store);


        mAuth = FirebaseAuth.getInstance();



    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbarmenu, menu);
        return true;
    }


    //TOOL BAR LOGOUT
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.logout:{

                mAuth.signOut();
                SharedPreferences sharedPreferences = this.getSharedPreferences("Availability", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit(); // clear availability

                startActivity(new Intent(MainActivity.this, login.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }




    //if the user is not logged in
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity((new Intent(MainActivity.this, login.class)));
        }

    }


}