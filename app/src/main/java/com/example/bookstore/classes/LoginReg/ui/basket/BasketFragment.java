package com.example.bookstore.classes.LoginReg.ui.basket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.classes.LoginReg.login;
import com.example.bookstore.classes.LoginReg.ui.checkout.Personal_Details;
import com.example.bookstore.classes.LoginReg.ui.store.StoreFragment;
import com.example.bookstore.classes.LoginReg.ui.store.book_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BasketFragment extends AppCompatActivity {



    //FIREBASE
    FirebaseAuth mAuth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference= db.getReference();

    //BUTTONS
    Button toStore,checkout,toCheckout;

    RecyclerView recyclerView;
    ArrayList<book_info> list;
    basketAdapter adapter;
    TextView totalPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_basket);

        mAuth = FirebaseAuth.getInstance();



        // SET RECYCLER VIEWER
        CommonFunctions callVar = new  CommonFunctions(this);

        list = callVar.loadData(true);


        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerB);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new basketAdapter(BasketFragment.this, list);
        recyclerView.setAdapter(adapter);


        //set total price

        if(list.size()>=1) {

            int sum = 0;

            totalPrice = findViewById(R.id.totalPrice);

            for (int position = 0; position < list.size(); position++) {


                sum = sum + Integer.parseInt(list.get(position).getPrice()) * Integer.parseInt(list.get(position).getQuantity());

            }

            totalPrice.setText("Total Price: " + Integer.toString(sum) + "€");

        }


        //checkout button pressed
        checkout = findViewById(R.id.checkout);

        //set checkout button visibility
        if(list.size() == 0){
            checkout.setVisibility(View.GONE);
            Toast.makeText(this, "your basket is empty!", Toast.LENGTH_SHORT).show();
        }

        checkout.setOnClickListener(view -> {

                startActivity(new Intent(BasketFragment.this, Personal_Details.class));//*******************

        });



        //store button pressed
        toStore = findViewById(R.id.store_button2);

        toStore.setOnClickListener(view -> {


                startActivity(new Intent(BasketFragment.this, StoreFragment.class));


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
                startActivity(new Intent(BasketFragment.this, login.class));
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
            startActivity((new Intent(BasketFragment.this, login.class)));
        }
    }


    //checkout button is clicked
    public void checkoutClick(final int position) {
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity((new Intent(BasketFragment.this, Personal_Details.class)));

            }
        });
    }



}