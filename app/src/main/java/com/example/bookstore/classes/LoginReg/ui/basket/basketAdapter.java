package com.example.bookstore.classes.LoginReg.ui.basket;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.R;
import com.example.bookstore.classes.LoginReg.ui.store.book_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class basketAdapter extends RecyclerView.Adapter<basketAdapter.MyViewHolder> {

    Context context;
    ArrayList<book_info> info;
    ArrayList<book_info> info2;

    int sum;

    public basketAdapter(Context c , ArrayList<book_info> p)
    {
        context = c;
        info = p;

        CommonFunctions callVar = new  CommonFunctions(this.context);

        info2 = callVar.loadData(true);
    }


    @NonNull
    @Override
    public basketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new basketAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.basket_card_view,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.name.setText(info.get(position).getName());
        holder.price.setText(info.get(position).getPrice()+"€");
        holder.quantity.setText(info.get(position).getQuantity());
        Picasso.get().load(info.get(position).getImage()).into(holder.image);




        //Delete is pressed
        holder.deleteButton(position);
    }






    @Override
    public int getItemCount() {
        return info.size();
    }





    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,price,quantity;
        ImageView image;
        ImageButton buyB;

        FirebaseAuth mAuth;
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        String usersId = user.getUid(); //get user's id




        //SET RECYCLE VIEWER
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameC);
            price = (TextView) itemView.findViewById(R.id.priceC);
            image = (ImageView) itemView.findViewById(R.id.imageC);
            quantity = (TextView) itemView.findViewById(R.id.QuantityC);
            buyB = (ImageButton) itemView.findViewById(R.id.deleteProductButton);
        }


        private void saveBookData(){
            SharedPreferences sharedPreferences = context.getSharedPreferences("AddedBooks", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();


            Gson gson = new Gson();
            String json = gson.toJson(info2);
            editor.putString(usersId, json);
            editor.apply();
        }






        //add button is clicked
        public void deleteButton(final int position) {
            buyB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("AddedBooks", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    Toast.makeText(context, info2.get(position).getName()+ " is removed from the basket!", Toast.LENGTH_SHORT).show();



                    //decrease quantity
                    info2.get(position).setQuantity(Integer.toString(Integer.parseInt(info2.get(position).getQuantity()) - 1));

                    //remove book
                    if( Integer.parseInt(info2.get(position).getQuantity()) == 0){

                        info2.remove(info2.get(position));

                    }

                    //remove user's saved basket
                    editor.remove(usersId);

                    //save again in shared prefs
                    saveBookData();

                }
            });
        }


    }
}