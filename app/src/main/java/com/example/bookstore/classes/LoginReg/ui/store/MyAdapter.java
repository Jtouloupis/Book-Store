package com.example.bookstore.classes.LoginReg.ui.store;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.classes.LoginReg.ui.basket.CommonFunctions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<book_info> info;
    ArrayList<book_info> info2;

    public MyAdapter activity_details;


    public static String bookNames;

    public MyAdapter(Context c, ArrayList<book_info> p) {
        context = c;
        info = p;

        CommonFunctions callVar = new  CommonFunctions(this.context);

        info2 = callVar.loadData(true);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CommonFunctions callVar2 = new  CommonFunctions(this.context);

        holder.name.setText(info.get(position).getName());
        holder.price.setText(info.get(position).getPrice()+"€");
        holder.availability.setText(callVar2.availability(info.get(position).getAvailability()));
        Picasso.get().load(info.get(position).getImage()).into(holder.image);

        //details button clicked
        holder.onClickDetails(position);

        //check availability and then add
        if (Integer.parseInt((info.get(position).getAvailability())) >= 1) {
            holder.onClick(position);
        }
    }


    @Override
    public int getItemCount() {
        return info.size();
    }

    FirebaseAuth mAuth;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    String usersId = user.getUid(); //get user's id



    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, availability;
        ImageView image;
        Button buyB, detailsB;


        //SET RECYCLE VIEWER
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            availability = (TextView) itemView.findViewById(R.id.availability);
            image = (ImageView) itemView.findViewById(R.id.image);

            detailsB = (Button) itemView.findViewById(R.id.details);
            buyB = (Button) itemView.findViewById(R.id.add2B);
        }



        //add button is clicked
        public void onClick(final int position) {
            buyB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    saveBookData(position);


                }
            });
        }






        private void saveBookData(int position1){
            SharedPreferences sharedPreferences = context.getSharedPreferences("AddedBooks", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();

            boolean found = false;
            int keepPosition=0;


            //check if book already exists in the basket list of the user

            for (int position2 = 0; position2 < info2.size(); position2++) {


                if (info.get(position1).getName().equals(info2.get(position2).getName())) {

                    found = true;

                    keepPosition = position2;

                }

            }
            //book already exists in the list
            if (found){

                if(Integer.parseInt(info.get(position1).getAvailability()) < Integer.parseInt(info2.get(keepPosition).getQuantity()) + 1){

                        Toast.makeText(context, info.get(position1).getName() + " is out of stock!", Toast.LENGTH_SHORT).show();

                }else {
                    //+1 for quantity  (uses constructor's availability for quantity)
                    info2.get(keepPosition).setQuantity( Integer.toString(Integer.parseInt(info2.get(keepPosition).getQuantity()) + 1));

                    Toast.makeText(context, "book is added in basket!", Toast.LENGTH_SHORT).show();
                }


            //save the new book in the basket list and set quantity to 1
            }else{

                if(Integer.parseInt(info.get(position1).getAvailability())>0){

                    //(uses constructor's availability for quantity)
                    info.get(position1).setQuantity("1");

                    info2.add(info.get(position1));

                    Toast.makeText(context, "book is added in basket!", Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(context, info.get(position1).getName() + " is out of stock!", Toast.LENGTH_SHORT).show();

                }


            }

            String json = gson.toJson(info2);

            editor.putString(usersId, json);
            editor.apply();
        }



        //details button is clicked
        public void onClickDetails(final int position) {
            detailsB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonFunctions callVar2 = new  CommonFunctions(v.getContext());
                    Intent myIntent = new Intent(v.getContext(), detailsAct.class);

                    //info.get(position).getImage()
                    myIntent.putExtra("image", info.get(position).getImage());
                    myIntent.putExtra("name", info.get(position).getName());
                    myIntent.putExtra("price", info.get(position).getPrice()+"€");
                    myIntent.putExtra("availability", callVar2.availability(info.get(position).getAvailability()));
                    myIntent.putExtra("details", info.get(position).getDetails());

                    context.startActivity(myIntent);
                }
            });
        }

    }



}
