package com.example.bookstore.classes.LoginReg.ui.store;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.classes.LoginReg.login;
import com.example.bookstore.classes.LoginReg.ui.basket.BasketFragment;
import com.example.bookstore.databinding.FragmentStoreBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StoreFragment extends AppCompatActivity {
    private FragmentStoreBinding binding;



    FirebaseAuth mAuth;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference= db.getReference();

    RecyclerView recyclerView;
    ArrayList<book_info> list;
    MyAdapter adapter;


    String usersId = user.getUid(); //get user's id

    //buttons
    Button toBasket;
    Button toStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_store);


        mAuth = FirebaseAuth.getInstance();

        toBasket = findViewById(R.id.basket_button1);


        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference().child("Books");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<book_info>();


                //loop in all books in database
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {

                    book_info p = dataSnapshot1.getValue(book_info.class);
                    list.add(p);

                }


                adapter = new MyAdapter(StoreFragment.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StoreFragment.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });





        //basket button pressed
        toBasket.setOnClickListener(view -> {
            startActivity(new Intent(StoreFragment.this, BasketFragment.class));
        });
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

                startActivity(new Intent(StoreFragment.this, login.class));
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
            startActivity((new Intent(StoreFragment.this, login.class)));
        }
    }


}