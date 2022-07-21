package com.example.bookstore.classes.LoginReg.ui.checkout;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore.R;
import com.example.bookstore.classes.LoginReg.ui.basket.CommonFunctions;
import com.example.bookstore.classes.LoginReg.ui.store.StoreFragment;
import com.example.bookstore.classes.LoginReg.ui.store.book_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Card_details extends AppCompatActivity {

    Button confirmOrder;
    TextView holderName, CardNumber, expDate, cvv, postalCode;
    ArrayList<book_info> list;

    //FIREBASE
    FirebaseAuth mAuth;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference reference= db.getReference();
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    String usersId = user.getUid(); //get user's id


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.card_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mAuth = FirebaseAuth.getInstance();


        confirmOrder = findViewById(R.id.checkout2);
        confirmOrder.setOnClickListener(view -> {

            if (checkForNulls()){

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

                startActivity(new Intent(Card_details.this, StoreFragment.class));
            }
        });



    }



    public boolean checkForNulls(){
        boolean empty = true;

        holderName = findViewById(R.id.holderName);
        CardNumber = findViewById(R.id.cardNum);
        expDate = findViewById(R.id.expDate);
        cvv = findViewById(R.id.cvv);
        postalCode = findViewById(R.id.postalCode);


        if(holderName.getText().toString().isEmpty() || CardNumber.getText().toString().isEmpty() || expDate.getText().toString().isEmpty() ||cvv.getText().toString().isEmpty() ||postalCode.getText().toString().isEmpty() ){

            empty = false;

            Toast.makeText(Card_details.this,"Please fill all the fields!", Toast.LENGTH_SHORT).show();

        }

        return empty;
    }
}
