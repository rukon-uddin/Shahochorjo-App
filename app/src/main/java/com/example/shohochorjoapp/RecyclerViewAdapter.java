package com.example.shohochorjoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularimageview.CircularImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    String name[], rank[];
    int image[];
    Context context;

    public RecyclerViewAdapter(Context ct, String n[], String d[], int i[]){
        context = ct;
        name = n;
        rank = d;
        image = i;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerrow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nametv.setText(name[position]);
        holder.ranktv.setText(rank[position]);
        holder.imageci.setImageResource(image[position]);

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nametv, ranktv;
        CircularImageView imageci;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nametv = itemView.findViewById(R.id.textView15);
            ranktv = itemView.findViewById(R.id.textView16);
            imageci = itemView.findViewById(R.id.imageView7);

        }
    }
}
