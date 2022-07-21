package com.example.bookstore.classes.LoginReg.ui.basket;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bookstore.classes.LoginReg.ui.store.book_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CommonFunctions {

    ArrayList<book_info> booklist;


    public CommonFunctions(Context context) {
        this.context = context;
    }

    Context context;
    FirebaseAuth mAuth;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    String usersId = user.getUid(); //get user's id

    public  ArrayList<book_info> loadData(boolean fromWhere) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("AddedBooks", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(usersId, null);
        Type type = new TypeToken<ArrayList<book_info>>() {}.getType();
        booklist = gson.fromJson(json, type);



        if (booklist == null) {
            booklist = new ArrayList<>();
        }


        return booklist;
    }



    public String availability(String number){

        if( parseInt(number) < 1){
            number ="out of stock";
        }else if( parseInt(number)>5){
            number ="high availability";
        }else if( parseInt(number) <= 5){
            number ="limited";
        }

        return number;
    }

}

//texts
//voice
