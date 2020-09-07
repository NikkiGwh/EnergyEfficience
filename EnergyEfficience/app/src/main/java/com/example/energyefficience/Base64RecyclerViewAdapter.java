package com.example.energyefficience;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Base64RecyclerViewAdapter extends  RecyclerView.Adapter<Base64RecyclerViewAdapter.MyBase64ViewHolder>{

    private final LayoutInflater mInflater;
    private List<Base64BlindTextEntity> elementsList;

    class MyBase64ViewHolder extends RecyclerView.ViewHolder {
        private final TextView base64TextView;
        private final TextView base64EncodedTextView;
        private MyBase64ViewHolder(View recyclerItemView) {
            super(recyclerItemView);
            base64TextView = recyclerItemView.findViewById(R.id.base64TextView);
            base64EncodedTextView = recyclerItemView.findViewById(R.id.base64EncodedTextView);
        }
    }


    public Base64RecyclerViewAdapter(Context context) {


        mInflater = LayoutInflater.from(context);
    }
    @Override
    public MyBase64ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerItemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new MyBase64ViewHolder(recyclerItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBase64ViewHolder holder, int position) {
        if(elementsList != null){
            Base64BlindTextEntity current = elementsList.get(position);
            holder.base64TextView.setText(current.getBlindText());
            holder.base64EncodedTextView.setText(current.getEncodedText());
        }else{
            //data not ready yet
            holder.base64TextView.setText("empty List");
        }
    }
    @Override
    public int getItemCount() {
        if(elementsList != null){
            return elementsList.size();
        }else
            return 0;
    }
    void setElementsList(List<Base64BlindTextEntity> elementsList){
        this.elementsList = elementsList;
        notifyDataSetChanged();
    }
}
